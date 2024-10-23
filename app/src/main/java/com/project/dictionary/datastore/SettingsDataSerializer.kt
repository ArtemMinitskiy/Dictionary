package com.project.dictionary.datastore

import androidx.datastore.core.Serializer
import com.project.dictionary.utils.Constants.BLUE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.InputStream
import java.io.OutputStream

class SettingsDataSerializer : Serializer<SettingsData> {
    override val defaultValue: SettingsData
        get() = SettingsData(
            color = BLUE,
        )

    override suspend fun readFrom(input: InputStream): SettingsData {
        return try {
            Json.decodeFromStream(
                SettingsData.serializer(),
                input
            )
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: SettingsData, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    SettingsData.serializer(),
                    t
                ).encodeToByteArray()
            )
            output.close()
        }
    }
}