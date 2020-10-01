package rocks.gioac96.veronica.core;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import rocks.gioac96.veronica.util.Tuple;

public class RadixRouter {

    @Getter
    @AllArgsConstructor
    static final class Route {

        private final String name;

    }

    private final class RouteTree {

        private final List<Tuple<Predicate<Request>, Route>> routes = new ArrayList<>();
        private final Map<String, RouteTree> children = new HashMap<>();
        private final List<Tuple<Predicate<Request>, Route>> starRoutes = new ArrayList<>();

    }

    private final Map<HttpMethod, RouteTree> routeTrees = new HashMap<>();

    private void propagateNewStarPath(
        RouteTree tree,
        Predicate<Request> condition,
        Route route
    ) {

        tree.starRoutes.add(new Tuple<>(condition, route));

        for (Map.Entry<String, RouteTree> child : tree.children.entrySet()) {

            propagateNewStarPath(child.getValue(), condition, route);

        }

    }

    private RouteTree createChild(
        RouteTree parent,
        String childPathPart
    ) {

        RouteTree child = new RouteTree();

        child.starRoutes.addAll(parent.starRoutes);

        parent.children.put(childPathPart, child);

        return child;

    }

    private void checkPathPatternStars(String pathPattern) {

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

    void register(
        String pathPattern,
        Predicate<Request> condition,
        Route route
    ) {

        for (HttpMethod httpMethod : HttpMethod.values()) {

            register(
                httpMethod,
                pathPattern,
                condition,
                route
            );

        }

    }

    void register(
        HttpMethod httpMethod,
        String pathPattern,
        Route route
    ) {

        register(
            httpMethod,
            pathPattern,
            request -> true,
            route
        );

    }

    void register(
        String pathPattern,
        Route route
    ) {

        register(
            pathPattern,
            request -> true,
            route
        );

    }

    void register(
        HttpMethod httpMethod,
        String pathPattern,
        Predicate<Request> condition,
        Route route
    ) {

        checkPathPatternStars(pathPattern);

        RouteTree pointer = routeTrees.get(httpMethod);

        if (pointer == null) {

            pointer = new RouteTree();
            routeTrees.put(httpMethod, pointer);

        }

        Iterator<String> pathPatternPartsIterator = Arrays.stream(getPathParts(pathPattern)).iterator();

        while (pathPatternPartsIterator.hasNext()) {

            String pathPatternPart = pathPatternPartsIterator.next();

            if (pathPatternPart.equals("*")) {

                // Propagating star path
                propagateNewStarPath(pointer, condition, route);
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

                        propagateNewStarPath(pointer, condition, route);

                    }

                    child = createChild(pointer, pathPatternPart);
                    pointer = child;

                }

                break;

            } else {

                pointer = child;

            }

            //TODO add variable path parts iteration

        }

        // Leaf found
        pointer.routes.add(new Tuple<>(condition, route));

    }

    private String[] getPathParts(String path) {

        if (path.length() == 0 || path.equals("/")) {

            return new String[0];

        }else if (path.startsWith("/")) {

            return path.substring(1).split("/");

        } else {

            return path.split("/");

        }

    }

    private Route getFirstRouteMatch(
        Request request,
        Collection<Tuple<Predicate<Request>, Route>> possibleRoutes
    ) {

        for (Tuple<Predicate<Request>, Route> starRoute : possibleRoutes) {

            if (starRoute.getFirst().test(request)) {

                return starRoute.getSecond();

            }

        }

        return null;

    }

    public Route route(
        Request request
    ) {

        RouteTree pointer = routeTrees.get(request.getHttpMethod());

        if (pointer == null) {

            return null;

        }

        String[] pathParts = getPathParts(request.getPath());

        for (String pathPart : pathParts) {

            RouteTree child = pointer.children.get(pathPart);

            if (child == null) {

                // Child not found
                return getFirstRouteMatch(
                    request,
                    pointer.starRoutes
                );

            } else {

                pointer = child;

            }

        }

        Route route = getFirstRouteMatch(request, pointer.routes);

        if (route == null) {

            return getFirstRouteMatch(request, pointer.starRoutes);

        }

        return route;

    }

}
