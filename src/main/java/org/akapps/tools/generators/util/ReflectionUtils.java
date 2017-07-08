package org.akapps.tools.generators.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

/**
 * Utility class about using Reflection
 *
 * @author Antoine Kapps
 */
public class ReflectionUtils {

    @SuppressWarnings("unchecked")
    private static final Map<Class<?>, Class<?>> PRIMITIVES = Collections.unmodifiableMap(ScriptBytecodeAdapter.createMap(new Object[] {
            boolean.class, Boolean.class,
            double.class, Double.class,
            float.class, Float.class,
            int.class, Integer.class,
            long.class, Long.class
    }));

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

    public static int distance(Class<?> type, Class<?> supertype) {
        if (supertype.isInterface())
            throw new RuntimeException("Not Implemented : interfaces are not supported... yet. (" + supertype + ")");

        int count = 0;
        Class<?> current = type;

        while (current != null && current != Object.class) {
            if (current == supertype)
                return count;

            count++;
            current = current.getSuperclass();
        }

        throw new IllegalArgumentException(String.format("%s is not a supertype of %s",
                supertype.getSimpleName(), type.getSimpleName()));
    }

    public static Class<?> getWrapperClassFor(Class<?> primitive) {
        final Class<?> wrapper = PRIMITIVES.get(primitive);

        if (wrapper == null) {
            if (primitive.isPrimitive())
                throw new IllegalStateException("It seems that the wrapper for " + primitive + " has been forgotten...");
            else
                throw new IllegalArgumentException(primitive + " is not a primitive class");
        }

        return wrapper;
    }

}
