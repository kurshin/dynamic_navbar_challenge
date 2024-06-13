package com.cinemo.test.data

import android.content.Context
import android.content.res.AssetManager
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStream

class AssetFileReaderTest {

    private lateinit var context: Context
    private lateinit var assetManager: AssetManager

    @Before
    fun setUp() {
        context = mockk()
        assetManager = mockk()

        every { context.assets } returns assetManager
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun readAssetFile_should_return_file_content() = runBlocking {
        val fileReader = AssetFileReader(context)
        val fileName = "test.json"
        val content = "line1\nline2"
        val inputStream: InputStream = ByteArrayInputStream(content.toByteArray())

        every { assetManager.open(fileName) } returns inputStream

        val actualContent = fileReader.readAssetFile(fileName)

        assertEquals(content, actualContent)

        verify { context.assets }
        verify { assetManager.open(fileName) }
        confirmVerified(context, assetManager)
    }

    @Test(expected = Exception::class)
    fun readAssetFile_should_throw_exception_if_file_not_found() {
        val fileReader = AssetFileReader(context)
        val fileName = "nonexistent.json"

        every { assetManager.open(fileName) } throws Exception("File not found")

        runBlocking {fileReader.readAssetFile(fileName)}
    }
}


