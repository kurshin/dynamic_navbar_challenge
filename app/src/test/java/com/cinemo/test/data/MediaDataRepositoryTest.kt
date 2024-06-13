package com.cinemo.test.data

import com.cinemo.data_parser.CinemoParser
import com.cinemo.data_parser.Result
import com.cinemo.test.domain.MediaData
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import java.io.IOException

class MediaDataRepositoryTest {

    private lateinit var fileReader: AssetFileReader
    private lateinit var parser: CinemoParser
    private lateinit var repository: MediaDataRepository

    @Before
    fun setUp() {
        fileReader = mockk()
        parser = mockk()
        repository = MediaDataRepository(fileReader, parser)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getMediaData should return Success when data is successfully read and parsed`() = runBlocking {
        val fileName = "media.json"
        val jsonString = """{"displayStyle": "grid", "items": []}"""
        val mediaData = MediaData(displayStyle = "grid", items = emptyList())

        coEvery { fileReader.readAssetFile(fileName) } returns jsonString
        every { parser.fromJson(jsonString, MediaData::class.java) } returns Result.Success(mediaData)

        val result = repository.getMediaData(fileName)

        assertTrue(result is Result.Success)
        assertEquals(mediaData, (result as Result.Success).data)

        coVerify { fileReader.readAssetFile(fileName) }
        verify { parser.fromJson(jsonString, MediaData::class.java) }
        confirmVerified(fileReader, parser)
    }

    @Test
    fun `getMediaData should return Error when file reading fails`() = runBlocking {
        val fileName = "media.json"
        val exception = IOException("File not found")

        coEvery { fileReader.readAssetFile(fileName) } throws exception

        val result = repository.getMediaData(fileName)

        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)

        coVerify { fileReader.readAssetFile(fileName) }
        confirmVerified(fileReader)
    }

    @Test
    fun `getMediaData should return Error when parsing fails`() = runBlocking {
        val fileName = "media.json"
        val jsonString = """{"displayStyle": "grid", "items": []}"""
        val exception = RuntimeException("Parsing error")

        coEvery { fileReader.readAssetFile(fileName) } returns jsonString
        every { parser.fromJson(jsonString, MediaData::class.java) } returns Result.Error(exception)

        val result = repository.getMediaData(fileName)

        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)

        coVerify { fileReader.readAssetFile(fileName) }
        verify { parser.fromJson(jsonString, MediaData::class.java) }
        confirmVerified(fileReader, parser)
    }
}
