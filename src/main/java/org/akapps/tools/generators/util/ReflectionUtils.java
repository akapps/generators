package org.akapps.tools.generators.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class about using Reflection
 *
 * @author Antoine Kapps
 */
public class ReflectionUtils {

    private ReflectionUtils() {
        throw new AssertionError("Not instantiable");
    }

    public static List<Class<?>> getClassHierarchy(Class<?> type) {
        Class<?> current = type;
        final List<Class<?>> result = new ArrayList<>();

        while (current != null && current != Object.class) {

            result.add(current);
            current = current.getSuperclass();
        }

        Collections.reverse(result);
        return result;
    }

}
