package com.xycz.simplelive.data.local.dao

import androidx.room.*
import com.xycz.simplelive.data.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for history operations
 */
@Dao
interface HistoryDao {

    @Query("SELECT * FROM history ORDER BY updateTime DESC")
    fun getAllHistory(): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history ORDER BY updateTime DESC LIMIT :limit")
    fun getRecentHistory(limit: Int): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history WHERE id = :id")
    suspend fun getHistoryById(id: String): HistoryEntity?

    @Query("SELECT * FROM history WHERE platform = :platform ORDER BY updateTime DESC")
    fun getHistoryByPlatform(platform: String): Flow<List<HistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: HistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(histories: List<HistoryEntity>)

    @Update
    suspend fun update(history: HistoryEntity)

    @Delete
    suspend fun delete(history: HistoryEntity)

    @Query("DELETE FROM history WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM history")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM history")
    suspend fun getCount(): Int
}