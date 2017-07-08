package org.akapps.tools.generators.code

import org.junit.Ignore
import org.junit.Test

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.atomic.AtomicInteger

class MappingGeneratorResolverTest {

    def resolver = new MappingGeneratorResolver()

    @Test(expected = NoSuchElementException)
    void findGenerator_UnRegistered() {
        resolver.findGenerator(AtomicInteger, new AtomicInteger())
    }

    @Test
    void register_ExplicitlyRegistered() {
        CodeGenerator<AtomicInteger> cg = {AtomicInteger o -> return "new AtomicInteger(${o.get()})"}
        resolver.register(AtomicInteger, cg)

        assert resolver.findGenerator(AtomicInteger, new AtomicInteger()).is(cg)
    }

    @Test
    void findGenerator_TypeFirst() {
        CodeGenerator<Number> numberGen = {}
        CodeGenerator<Integer> integerGen = {}
        resolver.register(Number, numberGen)
        resolver.register(Integer, integerGen)

        Integer value = 1
        assert resolver.findGenerator(Number, value).is(numberGen)
    }

    enum TestedEnum { ONE, TWO }

    @Test
    void findGenerator_ClosestMatch_EnumsExample() {
        CodeGenerator<Enum> gen = {}
        resolver.register(Enum, gen)

        assert resolver.findGenerator(TestedEnum, TestedEnum.ONE).is(gen)
    }

    @Test
    void findGenerator_ClosestMatch_CollectionsExample() {
        CodeGenerator<Collection> gen = {}
        resolver.register(Collection, gen)

        assert resolver.findGenerator(List, Collections.emptyList()).is(gen)
    }

    class Root {}
    class Middle extends Root {}
    class Leaf extends Middle {}

    @Test
    void findGenerator_ClosestMatch_HierarchyExample() {
        CodeGenerator<Root> rootGen = {}
        resolver.register(Root, rootGen)
        CodeGenerator<Middle> middleGen = {}
        resolver.register(Middle, middleGen)

        Leaf value = new Leaf()
        assert resolver.findGenerator(Leaf, value).is(middleGen)
    }

    interface BusinessType {}

    interface FinalBusinessType extends BusinessType {}

    @Test
    @Ignore("Interesting example, but not handled yet - and not necessary at this stage")
    void findGenerator_ClosestMatch_ProxyExample() {
        CodeGenerator<FinalBusinessType> rootGen = {}
        resolver.register(FinalBusinessType, rootGen)

        def proxy = Proxy.newProxyInstance(this.class.classLoader, [FinalBusinessType] as Class[], new InvocationHandler(){
            @Override
            Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return null
            }
        })
        assert proxy instanceof FinalBusinessType
        assert proxy instanceof BusinessType

        assert resolver.findGenerator(BusinessType, proxy).is(rootGen)
    }
}
