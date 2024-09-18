package com.project.dictionary.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.dictionary.model.Word

@Dao
interface WordsDao {
    @Query("SELECT * FROM Word")
    suspend fun getAllWords(): List<Word>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(dog: Word)

    @Delete
    suspend fun deleteWord(dog: Word)

}