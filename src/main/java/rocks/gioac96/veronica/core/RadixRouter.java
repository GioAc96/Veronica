package rocks.gioac96.veronica.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.NonNull;
import lombok.Setter;

public class RadixRouter {

    private static class Tree {

        private final String pathPart;

        private final Map<String, Tree> children = new HashMap<>();

        private final Map<HttpMethod, String> values = new HashMap<>();

        @Setter
        private String defaultValue = null;

        public Tree(@NonNull String pathPart) {

            this.pathPart = pathPart;

        }

    }

    private final Tree root = new Tree("");

    private static String[] getPathParts(String path) {

        if (path.length() == 0 || path.equals("/")) {

            return new String[0];

        }else if (path.startsWith("/")) {

            return path.substring(1).split("/");

        } else {

            return path.split("/");

        }

    }

    public String getValue(String path, HttpMethod httpMethod) {

        String[] pathParts = getPathParts(path);

        Tree pointer = root;

        pathParts:
        for (String pathPart : pathParts) {

            for (Map.Entry<String, Tree> child : pointer.children.entrySet()) {

                if (child.getKey().equals(pathPart)) {

                    pointer = child.getValue();
                    continue pathParts;

                }

            }

            return null;

        }

        if (pointer.values.containsKey(httpMethod)) {

            return pointer.values.get(httpMethod);

        } else {

            return pointer.defaultValue;

        }

    }

    public void register(String path, String value) {

        register(path, (Collection<HttpMethod>) null, value);

    }
    public void register(String path, HttpMethod httpMethod, String value) {

        register(path, List.of(httpMethod), value);

    }

    public void register(String path, Collection<HttpMethod> httpMethods, String value) {

        String[] pathParts = getPathParts(path);

        Tree pointer = root;

        pathParts:
        for (String pathPart : pathParts) {

            for (Map.Entry<String, Tree> child : pointer.children.entrySet()) {

                if (child.getKey().equals(pathPart)) {

                    pointer = child.getValue();
                    continue pathParts;

                }

            }

            Tree child = new Tree(pathPart);
            pointer.children.put(pathPart, child);
            pointer = child;

        }

        if (httpMethods == null) {

            pointer.defaultValue = value;

        } else {

            for (HttpMethod httpMethod : httpMethods) {

                pointer.values.put(httpMethod, value);

            }

        }

    }

}
