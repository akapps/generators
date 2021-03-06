package org.akapps.tools.generators.code.reflect

/**
 * Implementation based on the builder pattern.
 *
 * @author Antoine Kapps
 */
class BuilderNamingStrategy implements BeanNamingStrategy {

    @Override
    BeanNamingStrategy.Instanciator instantiateNew(Class beanType) {
        return new BeanNamingStrategy.Instanciator('builder',
                "new ${beanType.simpleName}Builder()")
    }

    @Override
    String setAttributeValue(String holderName, String attributeName, String generatedCodeValue) {
        return ".$attributeName($generatedCodeValue)"
    }

    @Override
    String join(List<String> lines) {
        return [*lines, ".build();"].join('\n    ')
    }
}
