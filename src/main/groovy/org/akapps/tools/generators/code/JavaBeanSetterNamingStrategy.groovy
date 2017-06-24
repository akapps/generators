package org.akapps.tools.generators.code

/**
 * Implementation based on the usage of JavaBean oriented setters.
 *
 * @author Antoine Kapps
 */
class JavaBeanSetterNamingStrategy implements BeanNamingStrategy {

    @Override
    BeanNamingStrategy.Instanciator instanciateNew(Class beanType) {
        def simpleName = beanType.simpleName
        def beanName = simpleName.uncapitalize()

        return new BeanNamingStrategy.Instanciator(beanName,
                "final $simpleName $beanName = new $simpleName();")
    }

    @Override
    String setAttributeValue(String holderName, String attributeName, String generatedCodeValue) {
        return "${holderName}.set${attributeName.capitalize()}($generatedCodeValue);"
    }

    @Override
    String join(List<String> lines) {
        return lines.join('\n')
    }
}
