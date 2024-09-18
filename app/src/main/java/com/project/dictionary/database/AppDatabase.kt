package com.project.dictionary.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.dictionary.model.Word

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordsDao(): WordsDao
}