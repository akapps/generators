package org.akapps.tools.generators.code.intern

import org.akapps.tools.generators.code.CodeGenerator
import org.akapps.tools.generators.code.GeneratorResolver

/**
 * A generator that handles the instantiation of Collections.
 * <p>
 *     Code for instantiation of the contained values is obtained by calling a {@link GeneratorResolver}
 *     for each one of them, then delegating the code generation to it.
 * </p>
 * <p>
 *     The collection type is deducted from the passed collection, but may not be the same. We use standard
 *     JDK collection types, with support for (at this stage, subject to changes) :
 *     <ul>
 *         <li>List &rarr; Arrays.asList(...)</li>
 *         <li>SortedSet &rarr; TreeSet</li>
 *         <li>LinkedHashSet &rarr; LinkedHashSet</li>
 *         <li>other Set &rarr; HashSet</li>
 *         <li>Queue &rarr; LinkedList</li>
 *         <li>other collections &rarr; Arrays.asList(...)</li>
 *     </ul>
 * </p>
 *
 * @author Antoine Kapps
 */
class CollectionCodeGenerator<C extends Collection> implements CodeGenerator<C> {

    private final GeneratorResolver generatorResolver

    CollectionCodeGenerator(GeneratorResolver generatorResolver) {
        this.generatorResolver = generatorResolver
    }

    @Override
    String asInstantiationCode(C value) {
        // We let Groovy find the most adapted version through dynamic method binding
        return codeConstruction(value)
    }

    private String withArrays(Collection values) {
        if (values.isEmpty())   return ""

        def colValues = values.collect { generatorResolver.findGenerator(it).asInstantiationCode(it) }
        return "Arrays.asList(${colValues.join(', ')})"
    }

    private String codeConstruction(List col) {
        return col.isEmpty() ? "Collections.emptyList()" : withArrays(col)
    }

    private String codeConstruction(SortedSet col) {
        return "new TreeSet<>(${withArrays(col)})"
    }

    private String codeConstruction(LinkedHashSet col) {
        return "new LinkedHashSet<>(${withArrays(col)})"
    }

    private String codeConstruction(Set col) {
        return "new HashSet<>(${withArrays(col)})"
    }

    private String codeConstruction(Queue col) {
        return "new LinkedList<>(${withArrays(col)})"
    }

    // default
    private String codeConstruction(Collection col) {
        return col.isEmpty() ? "Collections.emptySet()" : withArrays(col)
    }

}
