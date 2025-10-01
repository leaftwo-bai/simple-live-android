package com.xycz.simplelive.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.xycz.simplelive.data.local.converter.Converters
import com.xycz.simplelive.data.local.dao.FollowTagDao
import com.xycz.simplelive.data.local.dao.FollowUserDao
import com.xycz.simplelive.data.local.dao.HistoryDao
import com.xycz.simplelive.data.local.entity.FollowTagEntity
import com.xycz.simplelive.data.local.entity.FollowUserEntity
import com.xycz.simplelive.data.local.entity.HistoryEntity

/**
 * Simple Live Room database
 */
@Database(
    entities = [
        FollowUserEntity::class,
        HistoryEntity::class,
        FollowTagEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SimpleLiveDatabase : RoomDatabase() {

    abstract fun followUserDao(): FollowUserDao
    abstract fun historyDao(): HistoryDao
    abstract fun followTagDao(): FollowTagDao

    companion object {
        const val DATABASE_NAME = "simple_live.db"
    }
}