package com.xycz.simplelive.data.local.dao

import androidx.room.*
import com.xycz.simplelive.data.local.entity.FollowUserEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for follow user operations
 */
@Dao
interface FollowUserDao {

    @Query("SELECT * FROM follow_users ORDER BY addTime DESC")
    fun getAllFollows(): Flow<List<FollowUserEntity>>

    @Query("SELECT * FROM follow_users WHERE id = :id")
    suspend fun getFollowById(id: String): FollowUserEntity?

    @Query("SELECT * FROM follow_users WHERE siteId = :siteId ORDER BY addTime DESC")
    fun getFollowsBySite(siteId: String): Flow<List<FollowUserEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM follow_users WHERE id = :id)")
    suspend fun exists(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: FollowUserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<FollowUserEntity>)

    @Update
    suspend fun update(user: FollowUserEntity)

    @Delete
    suspend fun delete(user: FollowUserEntity)

    @Query("DELETE FROM follow_users WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM follow_users")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM follow_users")
    suspend fun getCount(): Int
}