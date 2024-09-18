package com.project.dictionary.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(@PrimaryKey @NonNull val wordId: String, val wordName: String, val wordDescription: String, val wordFullDescription: String = "", val wordLink: String = "", val wordImage: String = "")
