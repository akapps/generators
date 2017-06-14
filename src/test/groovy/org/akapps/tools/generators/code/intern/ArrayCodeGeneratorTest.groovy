package org.akapps.tools.generators.code.intern

import org.akapps.tools.generators.code.CodeGenerator
import org.akapps.tools.generators.code.GeneratorResolver
import org.junit.Test

class ArrayCodeGeneratorTest {

    CodeGenerator unitGenerator

    GeneratorResolver generatorResolver = { Object o -> unitGenerator}

    ArrayCodeGenerator generator = new ArrayCodeGenerator(generatorResolver)

    @Test(expected = IllegalArgumentException)
    void asInstantiationCode_NotAnArray() {
        def notAnArray = new Object()
        generator.asInstantiationCode(notAnArray)
    }

    @Test
    void asInstantiationCode_IntArray() {
        unitGenerator = {int i -> i.toString()}

        int[] array = [1, 2, 3, 4]
        assert generator.asInstantiationCode(array) == 'new int[]{ 1, 2, 3, 4 }'
    }

    @Test
    void asInstantiationCode_VerifyDelegation() {
        def count = 0
        unitGenerator = {int i -> "value${++count}" as String}

        int[] array = [1, 2, 3, 4]
        assert generator.asInstantiationCode(array) == 'new int[]{ value1, value2, value3, value4 }'
    }

    @Test
    void asInstantiationCode_EmptyArray() {
        String[] array = []
        assert generator.asInstantiationCode(array) == 'new String[0]'
    }
}
