package com.xycz.simplelive.data.local.dao

import androidx.room.*
import com.xycz.simplelive.data.local.entity.FollowTagEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for follow tag operations
 */
@Dao
interface FollowTagDao {

    @Query("SELECT * FROM follow_tags")
    fun getAllTags(): Flow<List<FollowTagEntity>>

    @Query("SELECT * FROM follow_tags WHERE id = :id")
    suspend fun getTagById(id: String): FollowTagEntity?

    @Query("SELECT * FROM follow_tags WHERE tag = :tag")
    suspend fun getTagByName(tag: String): FollowTagEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM follow_tags WHERE id = :id)")
    suspend fun exists(id: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM follow_tags WHERE tag = :tag)")
    suspend fun existsByName(tag: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag: FollowTagEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tags: List<FollowTagEntity>)

    @Update
    suspend fun update(tag: FollowTagEntity)

    @Delete
    suspend fun delete(tag: FollowTagEntity)

    @Query("DELETE FROM follow_tags WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM follow_tags")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM follow_tags")
    suspend fun getCount(): Int
}