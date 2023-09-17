package com.mohsen.nobka

import com.mohsen.nobka.platform.fileEmpty
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(MockKExtension::class)
class ExtensionsTest {

    /*
       There are three types of extension function in Kotlin:
       class-wide
       object-wide
       module-wide
       For an object or a class, you can mock extension functions just by creating a regular mockk
     */
    @Test
    fun `class-object extensions`() {
        with(mockk<Ext>()) {
            every { any(String::class).classEmpty() } returns "class empty"
            Assertions.assertEquals("class empty","hjbj".classEmpty())
        }

    }

    /*
      To mock module-wide extension functions you need to build mockkStatic(...) with the module’s class name as an argument.
      For example “pkg.FileKt” for module File.kt in the pkg package
     */
    @Test
    fun `file extensions`() {
        mockkStatic("com.mohsen.nobka.platform.ExtensionFileKt")
        every { any(String::class).fileEmpty() } returns "file empty"
        Assertions.assertEquals("file empty","any".fileEmpty())
    }

}

class Ext {
    fun String.classEmpty() = ""
}