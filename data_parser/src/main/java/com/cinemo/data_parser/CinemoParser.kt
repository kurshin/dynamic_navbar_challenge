package com.cinemo.data_parser

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class CinemoParser(private val gson: Gson = Gson()) {

    fun <T> fromJson(jsonString: String, classType: Class<T>): Result<T> {
        return try {
            val result = gson.fromJson(jsonString, classType)
            Result.Success(result)
        } catch (e: JsonSyntaxException) {
            Result.Error(e)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}