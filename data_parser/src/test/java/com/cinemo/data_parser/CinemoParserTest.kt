package com.cinemo.data_parser


import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CinemoParserTest {

    private lateinit var gson: Gson
    private lateinit var cinemoParser: CinemoParser

    @Before
    fun setUp() {
        gson = mockk()
        cinemoParser = CinemoParser(gson)
    }

    @Test
    fun `fromJson should return Success when parsing is successful`() {
        val jsonString = """{"name": "John Doe", "age": 30}"""
        val user = User("John Doe", 30)

        every { gson.fromJson(jsonString, User::class.java) } returns user

        val result = cinemoParser.fromJson(jsonString, User::class.java)

        assertTrue(result is Result.Success<*>)
        result as Result.Success
        assertEquals(user, result.data)
    }

    @Test
    fun `fromJson should return Error when parsing fails with JsonSyntaxException`() {
        val jsonString = """{"name": "John Doe", "age": "thirty"}"""

        every { gson.fromJson(jsonString, User::class.java) } throws JsonSyntaxException("Invalid JSON")

        val result = cinemoParser.fromJson(jsonString, User::class.java)

        assertTrue(result is Result.Error)
        result as Result.Error
        assertTrue(result.exception is JsonSyntaxException)
    }

    @Test
    fun `fromJson should return Error when parsing fails with general Exception`() {
        val jsonString = """{"name": "John Doe", "age": 30}"""

        every { gson.fromJson(jsonString, User::class.java) } throws RuntimeException("Parsing error")

        val result = cinemoParser.fromJson(jsonString, User::class.java)

        assertTrue(result is Result.Error)
        result as Result.Error
        assertTrue(result.exception is RuntimeException)
    }
}

data class User(val name: String, val age: Int)