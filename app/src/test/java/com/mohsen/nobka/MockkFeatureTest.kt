package com.mohsen.nobka

import com.google.gson.Gson
import com.mohsen.nobka.platform.CoroutineTestExtension
import com.mohsen.nobka.platform.execute
import io.mockk.checkUnnecessaryStub
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/*
  @MockKExtension.KeepMocks
  Finally, this extension will call unmockkAll in a @AfterAll callback,
  ensuring your test environment is clean after each test class execution.
  You can disable this behavior by adding the @MockKExtension.KeepMocks annotation to your class or
  globally by setting the mockk.junit.extension.keepmocks=true property
 */

/*
  @MockKExtension.ConfirmVerification
  You can make sure that all stubbed methods are actually verified by also annotating your test class with @MockKExtension.ConfirmVerification.
  This will internally call confirmVerified on all mocks after each test, to make sure there are no unnecessary stubbings.
  Please note that this behavior may not work as expected when running tests in your IDE, as it is Gradle who takes care of handling the exception being thrown when these confirmVerified calls fail.
 */

/*
  @MockKExtension.CheckUnnecessaryStub
  You can make sure that all stubbed methods are useful - used at least once - by also annotating your test class with @MockKExtension.CheckUnnecessaryStub.
  This will internally call checkUnnecessaryStub on all mocks after each test, to make sure there are no unnecessary stubbings.
 */

@ExtendWith(CoroutineTestExtension::class, MockKExtension::class)
//@MockKExtension.KeepMocks
//@MockKExtension.ConfirmVerification
//@MockKExtension.CheckUnnecessaryStub
class MockkFeatureTest {

    @Test
    fun `parameter injection`(@MockK list: List<String>) {
        every { list.size } returns 12
        //val size = list.size
        assert(12 == 12)
    }


    @Test
    fun `unnecessary stubbings with verify`(@MockK list: List<Int>) {
        every { list.size } returns 5
        list.size
        verify(exactly = 1) { list.size }
        //confirmVerified()
        assert(true)
    }

    @Test
    fun `unnecessary stubbings with unused`(@MockK list: List<Int>) {
        every { list.size } returns 5
        //checkUnnecessaryStub()
        assert(true)
    }

    /*
     Spies allow you to mix mocks and real objects.
     Note 1: the spy object is a copy of the passed object
     Note 2: there is a known issue if using a spy with a suspending function: https://github.com/mockk/mockk/issues/554
     */
    @Test
    fun `spy from Foo witout stubbing`(@SpyK foo: Foo) {
        val barResult = foo.bar()
        assert(barResult == "hello from bar")
    }

    @Test
    fun `mock from Foo witout stubbing`(@MockK foo: Foo) {
        val barResult = foo.bar()
        assert(barResult == "hello from bar")
    }

    /*
     A relaxed mock is the mock that returns some simple value for all functions.
     This allows you to skip specifying behavior for each case, while still stubbing things you need.
     For reference types, chained mocks are returned.
     */
    @Test
    fun `relaxed mock from Foo witout stubbing`(@RelaxedMockK foo: Foo) {
        val barResult = foo.bar()
        assert(barResult == "")
    }

    /*
     relaxed mocking is working badly with generic return types.
     A class cast exception is usually thrown in this case.
     Opt for stubbing manually in the case of a generic return type.
     */
    @Test
    fun `relaxed generic mock from Foo witout stubbing`(@RelaxedMockK foo: Foo) {
        val barResult = foo.genericBar("hello")
        assert(barResult == "")
    }


    /*
     Sometimes, you need to stub some functions, but still call the real method on others, or on specific arguments.
     This is possible by passing callOriginal() to answers, which works for both relaxed and non-relaxed mocks.
     */
    @Test
    fun `partial mocking`(@MockK foo: Foo) {
        every { foo.addOne(any()) } returns -1
        every { foo.addOne(3) } answers { callOriginal() }

        assertEquals(-1, foo.addOne(2))
        assertEquals(4, foo.addOne(3)) // original function is called
    }

    /*
      If you want Unit-returning functions to be relaxed, you can use relaxUnitFun = true as an argument to the mockk function
     */
    @Test
    fun `Mock relaxed for functions returning Unit`(@MockK(relaxUnitFun = true) foo: Foo) {
        foo.unitFun()
        assert(true)
    }

    /*
     Objects can be turned into mocks in the following way
     */
    @Test
    fun `Mocking object types`() {
        mockkObject(ObjBeingMocked) // applies mocking to an Object

        assertEquals(3, ObjBeingMocked.add(1, 2))

        every { ObjBeingMocked.add(1, 2) } returns 55
        assertEquals(55, ObjBeingMocked.add(1, 2))

        unmockkObject(ObjBeingMocked)
        // or unmockkAll()
    }

    @Test
    fun `Mocking object types with new instance`() {
        val newObjectMock = mockk<ObjBeingMocked>()

        //assertEquals(3, newObjectMock.add(1, 2))

        every { newObjectMock.add(1, 2) } returns 55
        assertEquals(55, newObjectMock.add(1, 2))

    }

    /*
     Enums can be mocked using mockkObject
     */
    @Test
    fun `enumeration mocks`() {
        mockkObject(Enumeration.CONSTANT)
        every { Enumeration.CONSTANT.goodInt } returns 42
        assertEquals(42, Enumeration.CONSTANT.goodInt)
    }

    /*
     Sometimes, especially in code you don’t own, you need to mock newly created objects.
     For this purpose, the following constructs are provided
     */
    @Test
    fun `constructor mocks`() {
        mockkConstructor(Gson::class)
        every { anyConstructed<Gson>().toJson("src") } returns "new src"
        val mockCls = MockCls()
        val pars = mockCls.parser()
        assertEquals("new src", pars)
    }

    /*
      You can mix both regular arguments and matchers:
     */
    @Test
    fun `argument with matchers`() {
        val fooMock = mockk<Foo>()
        val mockCls = MockCls()
        every {
            fooMock.funWithArgs(
                speed = more(10),
                enumeration = Enumeration.OTHER_CONSTANT,
                name = any(),
                long = range(10, 20),
                mockCls = eq(mockCls)
            )
        } returns "new arg"

        val result = fooMock.funWithArgs(
            speed = 20,
            enumeration = Enumeration.OTHER_CONSTANT,
            name = "zcz",
            long = 25,
            mockCls = mockCls
        )

        assertEquals("new arg", result)
    }

    /*
      You can stub chains of calls
     */
    @Test
    fun `chained calls`() {
        val gson = mockk<Gson>()
        every { gson.toJsonTree(any()).isJsonArray } returns false
        val isJsonArray = gson.toJsonTree("").isJsonArray
        assertEquals(isJsonArray, false)
    }

    /*
      From version 1.9.1 mocks may be chained into hierarchies:
     */
    @Test
    fun `hierarchical mocking`() {
        val addressBook = mockk<AddressBook> {
            every { contacts } returns listOf(
                mockk {
                    every { name } returns "John"
                    every { telephone } returns "123-456-789"
                    every { address.city } returns "New-York"
                    every { address.zip } returns "123-45"
                },
                mockk {
                    every { name } returns "Alex"
                    every { telephone } returns "789-456-123"
                    every { address } returns mockk {
                        every { city } returns "Wroclaw"
                        every { zip } returns "543-21"
                    }
                }
            )
        }
    }


    /*
     You can check the call count with the atLeast, atMost or exactly parameters
     */
    @Test
    fun `Verification atLeast, atMost or exactly times`() {
        val foo = mockk<Foo>(relaxed = true)

        foo.accelerate(fromSpeed = 10, toSpeed = 20)
        foo.accelerate(fromSpeed = 10, toSpeed = 30)
        foo.accelerate(fromSpeed = 20, toSpeed = 30)

        // all pass
        verify(atLeast = 3) { foo.accelerate(any(), any()) }
        verify(atMost = 2) { foo.accelerate(fromSpeed = 10, toSpeed = or(20, 30)) }
        verify(exactly = 1) { foo.accelerate(fromSpeed = 10, toSpeed = 20) }
        verify(exactly = 0) {
            foo.accelerate(
                fromSpeed = 30,
                toSpeed = 10
            )
        } // means no calls were performed

        confirmVerified(foo)
    }


    /*
     To verify concurrent operations, you can use timeout = xxx:
     */
    @Test
    fun `Verification timeout`() {
        mockk<Foo> {
            every { addOne(1) } returns 4

            Thread {
                Thread.sleep(2000)
                addOne(1)
            }.start()

            verify(timeout = 3000) { addOne(1) }
        }
    }

    /*
    Kotlin lets you declare functions that don’t belong to any class or object, called top-level functions.
    These calls are translated to static methods in jvm environments, and a special Java class is generated to hold the functions.
    These top-level functions can be mocked using mockkStatic. You just need to import the function and pass a reference as the argument:
     */
    @Test
    fun `Top Level functions`() {
        mockkStatic(::execute)
        every { execute() } returns 200
        val executeResult = execute()
        assertEquals(executeResult, 200)
    }

    /*
       IF you need to mock private functions, you can do it via a dynamic call
     */
    @Test
    fun `Private functions mocking-dynamic calls`(){
        val mock = spyk<Foo>(recordPrivateCalls = true)
        every { mock["pfun"]() } returns "private function"
        assertEquals("private function", mock.callFun())
        verify { mock["pfun"]() }
    }

}

class Foo {
    fun bar() = "hello from bar"

    fun <T> genericBar(input: T) = input

    fun addOne(num: Int) = num + 1

    fun unitFun() {}

    fun funWithArgs(
        speed: Int,
        enumeration: Enumeration,
        name: String,
        long: Int,
        mockCls: MockCls
    ): String = "args"

    fun accelerate(fromSpeed: Int, toSpeed: Int) {}

    private fun pfun():String = "private"
    fun callFun() = pfun()
    private val pVal:Int = 1

}

object ObjBeingMocked {
    fun add(a: Int, b: Int) = a + b
}

enum class Enumeration(val goodInt: Int) {
    CONSTANT(35),
    OTHER_CONSTANT(45);
}

class MockCls {
    fun parser(): String {
        return Gson().toJson("src")
    }
}

// Hierarchical mocking
interface AddressBook {
    val contacts: List<Contact>
}

interface Contact {
    val name: String
    val telephone: String
    val address: Address
}

interface Address {
    val city: String
    val zip: String
}


