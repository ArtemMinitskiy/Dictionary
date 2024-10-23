package com.project.dictionary.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

//Config object
data class StoreConfig<T>(
    val fileName: String,
    val serializer: Serializer<T>
)

//Skeletal classes for Proto-DataStore
abstract class ProtoDataStore<T>(open val context: Context, val config: StoreConfig<T>) {

    companion object {
        const val SETTINGS_DATA_PROTO = "settings_data.pb"
    }

    internal val protoDataStore: DataStore<T>
        get() = DataStoreFactory.create(
            serializer = config.serializer,
            scope = CoroutineScope(Dispatchers.IO),
            produceFile = { context.dataStoreFile(config.fileName) }
        )
}