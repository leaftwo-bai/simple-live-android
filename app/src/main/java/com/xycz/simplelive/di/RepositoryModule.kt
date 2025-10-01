package com.xycz.simplelive.di

import com.xycz.simplelive.data.repository.LiveRepositoryImpl
import com.xycz.simplelive.domain.repository.LiveRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for repository bindings
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLiveRepository(
        liveRepositoryImpl: LiveRepositoryImpl
    ): LiveRepository
}