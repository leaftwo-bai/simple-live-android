package com.xycz.simplelive.di

import com.xycz.simplelive.core.network.HttpClient
import com.xycz.simplelive.core.network.SimpleCookieJar
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Hilt module for network dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideCookieJar(): SimpleCookieJar {
        return SimpleCookieJar()
    }

    @Provides
    @Singleton
    @DefaultOkHttpClient
    fun provideDefaultOkHttpClient(
        cookieJar: SimpleCookieJar
    ): OkHttpClient {
        return HttpClient.createOkHttpClient(
            enableLogging = true,
            cookieJar = cookieJar
        )
    }

    @Provides
    @Singleton
    @BiliBiliOkHttpClient
    fun provideBiliBiliOkHttpClient(
        cookieJar: SimpleCookieJar
    ): OkHttpClient {
        return HttpClient.createOkHttpClient(
            enableLogging = true,
            cookieJar = cookieJar
        )
    }
}

/**
 * Qualifier for default OkHttpClient
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultOkHttpClient

/**
 * Qualifier for BiliBili OkHttpClient
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BiliBiliOkHttpClient