package org.akapps.tools.generators.util

import org.junit.Test

class ReflectionUtilsTest {

    @Test
    void getClassHierarchy_InOrder() {
        def hierarchy = ReflectionUtils.getClassHierarchy(ArrayList)
        assert hierarchy == [AbstractCollection, AbstractList, ArrayList]
    }

    @Test
    void distance() {
        assert ReflectionUtils.distance(ArrayList, AbstractCollection) == 2
        assert ReflectionUtils.distance(Integer, Number) == 1
        assert ReflectionUtils.distance(UnsupportedClassVersionError, Throwable) == 4
    }

    @Test(expected = IllegalArgumentException)
    void distance_inverted() {
        ReflectionUtils.distance(AbstractCollection, ArrayList)
    }

    @Test(expected = IllegalArgumentException)
    void distance_notSameHierarchy() {
        ReflectionUtils.distance(String, Number)
    }
}
