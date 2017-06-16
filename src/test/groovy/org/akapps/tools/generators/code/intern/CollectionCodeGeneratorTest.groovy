package org.akapps.tools.generators.code.intern

import org.akapps.tools.generators.code.CodeGenerator
import org.akapps.tools.generators.code.GeneratorResolver
import org.junit.Test

import java.util.concurrent.CopyOnWriteArraySet

class CollectionCodeGeneratorTest {

    CodeGenerator unitGenerator = {Object o -> o.toString()}

    GeneratorResolver generatorResolver = { Class c, Object o -> unitGenerator}

    CollectionCodeGenerator generator = new CollectionCodeGenerator(generatorResolver)

    @Test
    void asInstantiationCode_List() {
        List values = ["value1", "value2", "value3"]
        assert generator.asInstantiationCode(values) == 'Arrays.asList(value1, value2, value3)'
    }

    @Test
    void asInstantiationCode_SortedSet() {
        def values = Collections.unmodifiableSortedSet(new TreeSet(["value1", "value2", "value3"]))
        assert generator.asInstantiationCode(values) == 'new TreeSet<>(Arrays.asList(value1, value2, value3))'
    }

    @Test
    void asInstantiationCode_LinkedSet() {
        LinkedHashSet values = ["value1", "value2", "value3"]
        assert generator.asInstantiationCode(values) == 'new LinkedHashSet<>(Arrays.asList(value1, value2, value3))'
    }

    @Test
    void asInstantiationCode_OtherSet() {
        def values = new CopyOnWriteArraySet(["value1", "value2", "value3"])
        assert generator.asInstantiationCode(values) == 'new HashSet<>(Arrays.asList(value1, value2, value3))'
    }

    @Test
    void asInstantiationCode_Queue() {
        def values = new ArrayDeque(["value1", "value2", "value3"])
        assert generator.asInstantiationCode(values) == 'new LinkedList<>(Arrays.asList(value1, value2, value3))'
    }

    @Test
    void asInstantiationCode_OtherCollection() {
        def values = Collections.unmodifiableCollection(["value1", "value2", "value3"])
        assert generator.asInstantiationCode(values) == 'Arrays.asList(value1, value2, value3)'
    }

    @Test
    void asInstantiationCode_EmptyCollection() {
        // I want a really untyped collection
        def values = new AbstractCollection() {
            @Override Iterator iterator() { return Collections.emptyIterator()}
            @Override int size() { return 0 }
        }

        assert generator.asInstantiationCode(values) == 'Collections.emptySet()'
    }

    @Test
    void asInstantiationCode_EmptyList() {
        List values = []
        assert generator.asInstantiationCode(values) == 'Collections.emptyList()'
    }

    @Test
    void asInstantiationCode_EmptyTypedCollection() {
        def values = Collections.unmodifiableSortedSet(new TreeSet())
        assert generator.asInstantiationCode(values) == 'new TreeSet<>()'
    }
}
