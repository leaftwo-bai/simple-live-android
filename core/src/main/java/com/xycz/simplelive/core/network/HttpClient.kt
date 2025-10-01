package com.xycz.simplelive.core.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/**
 * HTTP client factory for creating Retrofit instances
 */
object HttpClient {

    private const val CONNECT_TIMEOUT = 15L
    private const val READ_TIMEOUT = 30L
    private const val WRITE_TIMEOUT = 30L

    /**
     * JSON configuration for kotlinx.serialization
     */
    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    /**
     * Create a base OkHttpClient with common configuration
     */
    fun createOkHttpClient(
        enableLogging: Boolean = false,
        cookieJar: okhttp3.CookieJar? = null,
        interceptors: List<okhttp3.Interceptor> = emptyList()
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .apply {
                // Add custom interceptors
                interceptors.forEach { addInterceptor(it) }

                // Add logging interceptor if enabled
                if (enableLogging) {
                    addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                }

                // Add cookie jar if provided
                if (cookieJar != null) {
                    cookieJar(cookieJar)
                }
            }
            .build()
    }

    /**
     * Create a Retrofit instance
     */
    fun createRetrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient = createOkHttpClient()
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}

/**
 * Custom interceptor for adding headers
 */
class HeaderInterceptor(
    private val headers: Map<String, String>
) : okhttp3.Interceptor {
    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
        val request = chain.request().newBuilder().apply {
            headers.forEach { (key, value) ->
                addHeader(key, value)
            }
        }.build()
        return chain.proceed(request)
    }
}

/**
 * User-Agent constants
 */
object UserAgents {
    const val WEB = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
    const val MOBILE = "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.6099.144 Mobile Safari/537.36"
}