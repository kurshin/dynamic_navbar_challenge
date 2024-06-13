package com.cinemo.test.data

import com.cinemo.data_parser.CinemoParser
import com.cinemo.test.domain.MediaData
import com.cinemo.data_parser.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaDataRepository(private val fileReader: AssetFileReader, private val parser: CinemoParser) {

    suspend fun getMediaData(fileName: String): Result<MediaData> {
        return withContext(Dispatchers.IO) {
            try {
                val jsonString = fileReader.readAssetFile(fileName)
                parser.fromJson(jsonString, MediaData::class.java)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}