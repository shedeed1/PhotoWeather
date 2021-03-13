package com.robusta.photoweather.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the History class.
 */
@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY locationName")
    fun getHistory(): Flow<List<History>>

    @Query("SELECT * FROM history WHERE id = :picId")
    fun getPic(picId: String): Flow<History>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(history: List<History>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: History)
}