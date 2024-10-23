package com.project.dictionary.datastore
import kotlinx.serialization.Serializable

@Serializable
data class SettingsData(
    val color: String
)