package com.cinemo.test.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class AssetFileReader(private val context: Context) {
    suspend fun readAssetFile(fileName: String): String {
        return withContext(Dispatchers.IO) {
            val assetManager = context.assets
            val inputStream = assetManager.open(fileName)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            bufferedReader.use { it.readText() }
        }
    }
}