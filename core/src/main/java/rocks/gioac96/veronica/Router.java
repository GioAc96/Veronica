package rocks.gioac96.veronica;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import lombok.NonNull;
import rocks.gioac96.veronica.providers.ConfigurableProvider;
import rocks.gioac96.veronica.providers.Provider;
import rocks.gioac96.veronica.util.Tuple;

public class Router implements RequestHandler {

    private final Map<HttpMethod, RouteTree> methodRouteTrees;
    private final RequestHandler defaultRequestHandler;
    private final List<RoutingGuard> routingGuards;
    private final String pathPrefix;

    public Router(RouterBuilder b) {

        this.methodRouteTrees = b.methodRouteTrees;
        this.defaultRequestHandler = b.defaultRequestHandler;
        this.routingGuards = b.routingGuards;
        this.pathPrefix = b.pathPrefix;

    }

    public static RouterBuilder builder() {

        return new RouterBuilder();

    }

    private static void checkPathPatternStars(String pathPattern) {

        StringCharacterIterator characterIterator = new StringCharacterIterator(pathPattern);

        int i = 0;

        while (characterIterator.current() != CharacterIterator.DONE) {

            if (characterIterator.current() == '*' && i != pathPattern.length() - 1) {

                throw new IllegalArgumentException();

            }

            characterIterator.next();
            i++;

        }

    }

    private String[] getPathParts(String path) {

        if (pathPrefix == null) {

            if (path.equals("/")) {

                return new String[0];

            } else if (path.startsWith("/")) {

                return path.substring(1).split("/");

            } else {

                return null;

            }


        } else if (path.startsWith(pathPrefix)) {

            String pathWithoutPrefix = path.replaceFirst("^" + pathPrefix, "");

            if (pathWithoutPrefix.startsWith("/")) {

                return pathWithoutPrefix.substring(1).split("/");

            } else if (pathWithoutPrefix.equals("")) {

                return new String[0];

            }

        }

        return null;

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

    public Response handle(
        Request request
    ) {

        for (RoutingGuard routingGuard : routingGuards) {

            Response response = routingGuard.handle(request);

            if (response != null) {

                return response;

            }

        }

        RequestHandler requestHandler = routeRequestInRoutesTree(request);

        if (requestHandler == null) {

            return defaultRequestHandler.handle(request);

        } else {

            return requestHandler.handle(request);

        }

    }

    private RequestHandler routeRequestInRoutesTree(
        Request request
    ) {

        RouteTree pointer = methodRouteTrees.get(request.getHttpMethod());

        if (pointer == null) {

            return null;

        }

        String[] pathParts = getPathParts(request.getPath());

        if (pathParts == null) {
            return null;
        }

        for (String pathPart : pathParts) {

            RouteTree child = pointer.children.get(pathPart);

            if (child == null) {

                // Checking for variable parts children
                if (pointer.variablePathPartChild != null) {

                    request.getVariablePathParts().put(
                        pointer.variablePathPartChild.getFirst(),
                        pathPart
                    );

                    pointer = pointer.variablePathPartChild.getSecond();

                    continue;

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

    protected static final class RouteTree {

        private final List<Tuple<Predicate<Request>, RequestHandler>> routes = new ArrayList<>();
        private final List<Tuple<Predicate<Request>, RequestHandler>> starRoutes = new ArrayList<>();
        private final Map<String, RouteTree> children = new HashMap<>();
        private Tuple<String, RouteTree> variablePathPartChild = null;

    }

    public static class RouterBuilder extends ConfigurableProvider<Router> {

        private final List<Route> routesToRegister = new LinkedList<>();

        protected Map<HttpMethod, RouteTree> methodRouteTrees = new HashMap<>();
        protected List<RoutingGuard> routingGuards = new LinkedList<>();
        protected RequestHandler defaultRequestHandler;
        protected String pathPrefix = null;


        @Override
        protected boolean isValid() {

            return methodRouteTrees != null
                && routingGuards != null
                && defaultRequestHandler != null
                && (pathPrefix == null || (pathPrefix.startsWith("/") & !pathPrefix.endsWith("/"))); // TODO throw an exception

        }

        @Override
        protected Router instantiate() {

            for (Route route : routesToRegister) {

                Predicate<Request> aggregateCondition = aggregateMatchingConditions(route.getRequestMatcher());

                Set<HttpMethod> httpMethods = aggregateHttpMethods(route.getRequestMatcher());

                for (HttpMethod httpMethod : httpMethods) {

                    for (String pathPattern : route.getRequestMatcher().getPathPatterns()) {

                        register(
                            httpMethod,
                            pathPattern,
                            aggregateCondition,
                            route.getRequestHandler()
                        );

                    }

                }

            }

            return new Router(this);

        }

        private static Set<HttpMethod> aggregateHttpMethods(RequestMatcher requestMatcher) {

            if (requestMatcher.getHttpMethods().size() == 0) {

                return new HashSet<>() {{

                    addAll(Arrays.asList(HttpMethod.values()));

                }};

            } else {

                return requestMatcher.getHttpMethods();

            }
        }

        private static Predicate<Request> aggregateMatchingConditions(RequestMatcher requestMatcher) {

            Predicate<Request> aggregateCondition;

            if (requestMatcher.getConditions().size() > 0) {

                Iterator<Predicate<Request>> conditionsIterator = requestMatcher.getConditions().iterator();

                aggregateCondition = conditionsIterator.next();

                while (conditionsIterator.hasNext()) {

                    aggregateCondition = aggregateCondition.and(conditionsIterator.next());

                }

            } else {

                aggregateCondition = request -> true;

            }

            return aggregateCondition;

        }

        public RouterBuilder defaultRequestHandler(@NonNull RequestHandler requestHandler) {

            this.defaultRequestHandler = requestHandler;
            return this;

        }

        public RouterBuilder defaultRequestHandler(@NonNull Provider<RequestHandler> requestHandlerProvider) {

            return defaultRequestHandler(requestHandlerProvider.provide());

        }

        public RouterBuilder route(@NonNull Route route) {

            this.routesToRegister.add(route);

            return this;

        }

        public RouterBuilder route(@NonNull Provider<Route> routeProvider) {

            return route(routeProvider.provide());

        }

        public RouterBuilder route(
            @NonNull String pathPattern,
            @NonNull RequestHandler requestHandler
        ) {

            return route(Route.builder()
                .requestMatcher(RequestMatcher.builder()
                    .pathPattern(pathPattern)
                    .provide())
                .requestHandler(requestHandler)
                .provide()
            );

        }

        public RouterBuilder route(
            @NonNull HttpMethod httpMethod,
            @NonNull String pathPattern,
            @NonNull RequestHandler requestHandler
        ) {

            return route(Route.builder()
                .requestMatcher(RequestMatcher.builder()
                    .pathPattern(pathPattern)
                    .httpMethod(httpMethod)
                    .provide())
                .requestHandler(requestHandler)
                .provide()
            );

        }

        public RouterBuilder route(
            @NonNull RequestMatcher requestMatcher,
            @NonNull RequestHandler requestHandler
        ) {

            return route(Route.builder()
                .requestMatcher(requestMatcher)
                .requestHandler(requestHandler)
                .provide()
            );

        }

        public RouterBuilder routingGuard(@NonNull RoutingGuard routingGuard) {

            routingGuards.add(routingGuard);

            return this;

        }

        public RouterBuilder routingGuard(@NonNull Provider<RoutingGuard> routingGuardProvider) {

            return routingGuard(routingGuardProvider.provide());

        }

        public RouterBuilder pathPrefix(@NonNull String pathPrefix) {

            this.pathPrefix = pathPrefix;

            return this;

        }

        public RouterBuilder pathPrefix(@NonNull Provider<String> pathPrefixProvider) {

            return pathPrefix(pathPrefixProvider.provide());

        }

        private String[] getPathPatternParts(
            String pathPattern
        ) {

            if (pathPattern.length() == 0 || pathPattern.equals("/")) {

                return new String[0];

            } else if (pathPattern.startsWith("/")) {

                return pathPattern.substring(1).split("/");

            } else {

                return pathPattern.split("/");

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

            Iterator<String> pathPatternPartsIterator = Arrays.stream(getPathPatternParts(pathPattern)).iterator();

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
                    && pathPatternPart.charAt(pathPatternPart.length() - 1) == '}'
            ) {

                parent.variablePathPartChild = new Tuple<>(
                    pathPatternPart.substring(1, pathPatternPart.length() - 1),
                    child
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

    }

}
