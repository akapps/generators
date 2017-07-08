package org.akapps.tools.generators.code.reflect

import groovy.transform.Immutable

/**
 * Strategy to define a bean in a {@link ReflectionBeanCodeGenerator}.
 *
 * @author Antoine Kapps
 */
interface BeanNamingStrategy {

    @Immutable
    static class Instanciator { String variableName, instantiationCode }

    /**
     * How to instantiate the new object
     *
     * @param beanType class of the bean to be generated
     * @return both the variable name and the code for the instantiation
     */
    Instanciator instantiateNew(Class beanType)

    /**
     * Adds a value for one of the bean's attributes
     *
     * @param holderName name of the variable used for the new object
     * @param attributeName name of the attribute beeing generated
     * @param generatedCodeValue generated code for the value of that attribute
     * @return code to add this attribute to the generated bean
     */
    String setAttributeValue(String holderName, String attributeName, String generatedCodeValue)

    /**
     * Joins all generated lines into a single result
     *
     * @param lines all generated lines of code
     * @return one String (possibly multi-lines) for the whole object creation
     */
    String join(List<String> lines)

}