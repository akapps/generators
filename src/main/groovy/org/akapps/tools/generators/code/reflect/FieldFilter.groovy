package org.akapps.tools.generators.code.reflect

import java.lang.reflect.Field

/**
 * @author Antoine Kapps
 */
interface FieldFilter {

    boolean exclude(Field field)

}