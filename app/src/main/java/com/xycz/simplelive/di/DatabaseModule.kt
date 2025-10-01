package com.xycz.simplelive.di

import android.content.Context
import androidx.room.Room
import com.xycz.simplelive.data.local.SimpleLiveDatabase
import com.xycz.simplelive.data.local.dao.FollowTagDao
import com.xycz.simplelive.data.local.dao.FollowUserDao
import com.xycz.simplelive.data.local.dao.HistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing database dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): SimpleLiveDatabase {
        return Room.databaseBuilder(
            context,
            SimpleLiveDatabase::class.java,
            SimpleLiveDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideFollowUserDao(database: SimpleLiveDatabase): FollowUserDao {
        return database.followUserDao()
    }

    @Provides
    @Singleton
    fun provideHistoryDao(database: SimpleLiveDatabase): HistoryDao {
        return database.historyDao()
    }

    @Provides
    @Singleton
    fun provideFollowTagDao(database: SimpleLiveDatabase): FollowTagDao {
        return database.followTagDao()
    }
}