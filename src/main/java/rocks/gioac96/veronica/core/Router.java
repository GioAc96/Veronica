package rocks.gioac96.veronica.core;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.util.Tuple;

public class Router {

    @Getter
    @AllArgsConstructor
    protected static final class VariablePathPartMatcher {

        private final Predicate<String> pathPartCondition;

        private final String name;

    }

    protected static final class RouteTree {

        private final List<Tuple<Predicate<Request>, RequestHandler>> routes = new ArrayList<>();

        private final List<Tuple<Predicate<Request>, RequestHandler>> starRoutes = new ArrayList<>();

        private final Map<String, RouteTree> children = new HashMap<>();

        private final List<Tuple<VariablePathPartMatcher, RouteTree>> variablePathPartChildren = new ArrayList<>();

    }

    private final Map<HttpMethod, RouteTree> methodRouteTrees;
    private final RequestHandler defaultRequestHandler;

    public Router(RouterBuilder b) {

        this.methodRouteTrees = b.methodRouteTrees;
        this.defaultRequestHandler = b.defaultRequestHandler;

    }

    public static RouterBuilder builder() {

        class RouterBuilderImpl extends RouterBuilder implements BuildsMultipleInstances {

        }

        return new RouterBuilderImpl();

    }

    private static void checkPathPatternStars(String pathPattern) {

        StringCharacterIterator characterIterator = new StringCharacterIterator(pathPattern);

        int i = 0;

        while(characterIterator.current() != CharacterIterator.DONE) {

            if (characterIterator.current() == '*' && i != pathPattern.length() - 1) {

                throw new IllegalArgumentException();

            }

            characterIterator.next();
            i++;

        }

    }

    private static String[] getPathParts(String path) {

        if (path.length() == 0 || path.equals("/")) {

            return new String[0];

        }else if (path.startsWith("/")) {

            return path.substring(1).split("/");

        } else {

            return path.split("/");

        }

    }

    private RequestHandler getFirstEligibleRequestHandler(
        Request request,
        Collection<Tuple<Predicate<Request>, RequestHandler>> possibleRoutes
    ) {

        for (Tuple<Predicate<Request>, RequestHandler> starRoute : possibleRoutes) {

            if (starRoute.getFirst().test(request)) {

                return starRoute.getSecond();

            }

        }

        return null;

    }

    public RequestHandler route(
        Request request
    ) {

        RequestHandler requestHandler = routeRequest(request);

        if (requestHandler == null) {

            return defaultRequestHandler;

        } else {

            return requestHandler;

        }

    }

    private RequestHandler routeRequest(
        Request request
    ) {

        RouteTree pointer = methodRouteTrees.get(request.getHttpMethod());

        if (pointer == null) {

            return null;

        }

        String[] pathParts = getPathParts(request.getPath());

        pathParts:
        for (String pathPart : pathParts) {

            RouteTree child = pointer.children.get(pathPart);

            if (child == null) {

                // Checking for variable parts children
                for (Tuple<VariablePathPartMatcher, RouteTree> variablePathRoute : pointer.variablePathPartChildren) {

                    if (variablePathRoute.getFirst().pathPartCondition.test(pathPart)) {

                        request.getVariablePathParts().put(
                            variablePathRoute.getFirst().name,
                            pathPart
                        );

                        pointer = variablePathRoute.getSecond();

                        continue pathParts;

                    }

                }

                // Child not found
                return getFirstEligibleRequestHandler(
                    request,
                    pointer.starRoutes
                );

            } else {

                pointer = child;

            }

        }

        RequestHandler requestHandler = getFirstEligibleRequestHandler(request, pointer.routes);

        if (requestHandler == null) {

            return getFirstEligibleRequestHandler(request, pointer.starRoutes);

        }

        return requestHandler;

    }

    public abstract static class RouterBuilder extends Builder<Router> {

        private RequestHandler defaultRequestHandler;

        private final Map<HttpMethod, RouteTree> methodRouteTrees = new HashMap<>();

        public RouterBuilder defaultRequestHandler(@NonNull RequestHandler requestHandler) {

            this.defaultRequestHandler = requestHandler;
            return this;

        }

        public RouterBuilder defaultRequestHandler(@NonNull Provider<RequestHandler> requestHandler) {

            return defaultRequestHandler(requestHandler.provide());

        }

        public RouterBuilder route(@NonNull Route route) {

            return register(route.getRequestMatcher(), route.getRequestHandler());

        }

        public RouterBuilder route(@NonNull Provider<Route> route) {

            return route(route.provide());

        }

        public RouterBuilder register(
            @NonNull RequestMatcher requestMatcher,
            @NonNull RequestHandler requestHandler
        ) {

            Predicate<Request> aggregateCondition = aggregateMatchingConditions(requestMatcher);

            Set<HttpMethod> httpMethods = parseHttpMethodSet(requestMatcher);

            for (HttpMethod httpMethod : httpMethods) {

                for (String pathPattern : requestMatcher.getPathPatterns()) {

                    register(
                        httpMethod,
                        pathPattern,
                        aggregateCondition,
                        requestHandler
                    );

                }

            }

            return this;

        }

        private static Set<HttpMethod> parseHttpMethodSet(RequestMatcher requestMatcher) {

            if (requestMatcher.getHttpMethods().size() == 0) {

                return new HashSet<>() {{

                    addAll(Arrays.asList(HttpMethod.values()));

                }};

            } else {

                return requestMatcher.getHttpMethods();

            }
        }

        private void register(
            HttpMethod httpMethod,
            String pathPattern,
            Predicate<Request> condition,
            RequestHandler requestHandler
        ) {

            checkPathPatternStars(pathPattern);

            RouteTree pointer = methodRouteTrees.get(httpMethod);

            if (pointer == null) {

                pointer = new RouteTree();
                methodRouteTrees.put(httpMethod, pointer);

            }

            Iterator<String> pathPatternPartsIterator = Arrays.stream(getPathParts(pathPattern)).iterator();

            while (pathPatternPartsIterator.hasNext()) {

                String pathPatternPart = pathPatternPartsIterator.next();

                if (pathPatternPart.equals("*")) {

                    // Propagating star path
                    propagateNewStarPath(pointer, condition, requestHandler);
                    return;

                }

                // Looking for child
                RouteTree child = pointer.children.get(pathPatternPart);

                if (child == null) {

                    // Child not found, expanding tree

                    child = createChild(pointer, pathPatternPart);
                    pointer = child;

                    while (pathPatternPartsIterator.hasNext()) {

                        pathPatternPart = pathPatternPartsIterator.next();

                        if (pathPatternPart.equals("*")) {

                            propagateNewStarPath(pointer, condition, requestHandler);

                        } else {

                            child = createChild(pointer, pathPatternPart);
                            pointer = child;

                        }

                    }

                    break;

                } else {

                    pointer = child;

                }

            }

            // Leaf found
            pointer.routes.add(new Tuple<>(condition, requestHandler));

        }

        private RouteTree createChild(
            RouteTree parent,
            String pathPatternPart
        ) {

            RouteTree child = new RouteTree();

            child.starRoutes.addAll(parent.starRoutes);

            if (
                pathPatternPart.charAt(0) == '{'
                    && pathPatternPart.charAt(pathPatternPart.length() -1) == '}'
            ) {

                parent.variablePathPartChildren.add(
                    new Tuple<>(
                        new VariablePathPartMatcher(
                            pathPart -> true,
                            pathPatternPart.substring(1, pathPatternPart.length() - 1)
                        ),
                        child
                    )
                );

            } else {

                parent.children.put(pathPatternPart, child);

            }


            return child;

        }

        private void propagateNewStarPath(
            RouteTree tree,
            Predicate<Request> condition,
            RequestHandler route
        ) {

            tree.starRoutes.add(new Tuple<>(condition, route));

            for (Map.Entry<String, RouteTree> child : tree.children.entrySet()) {

                propagateNewStarPath(child.getValue(), condition, route);

            }

        }

        private static Predicate<Request> aggregateMatchingConditions(RequestMatcher requestMatcher) {

            Predicate<Request> aggregateCondition;

            if (requestMatcher.getConditions().size() > 0) {

                Iterator<Predicate<Request>> conditionsIterator = requestMatcher.getConditions().iterator();

                aggregateCondition = conditionsIterator.next();

                while(conditionsIterator.hasNext()) {

                    aggregateCondition = aggregateCondition.and(conditionsIterator.next());

                }

            } else {

                aggregateCondition = request -> true;

            }

            return aggregateCondition;

        }


        @Override
        protected boolean isValid() {

            return super.isValid()
                && defaultRequestHandler != null;

        }

        @Override
        protected Router instantiate() {

            return new Router(this);

        }

    }

}
