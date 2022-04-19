package com.nsp.timetracker.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.nsp.timetracker.BuildConfig
import com.nsp.timetracker.base.Constants.Companion.API_URL
import com.nsp.timetracker.base.Constants.Companion.API_V3
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiInitHelper {

    private val okHttpClientBuilder = OkHttpClient.Builder()
    private var retrofit: Retrofit? = null

    private fun okHttpClient(): OkHttpClient {
        when {
            BuildConfig.DEBUG -> {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                okHttpClientBuilder.addInterceptor(logging)
            }
        }
        return okHttpClientBuilder.build()
    }

    fun getClient(baseUrl: String = API_URL): Retrofit {
        return getClient().newBuilder().baseUrl(baseUrl).build()
    }

    fun getClient(): Retrofit {
        when (retrofit) {
            null -> {
                retrofit = Retrofit.Builder()
                    .addConverterFactory(
                        MoshiConverterFactory.create(
                            Moshi.Builder()
                                .add(KotlinJsonAdapterFactory())
                                .build()
                        )
                    )
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .client(okHttpClient())
                    .baseUrl(API_V3)
                    .build()
            }
        }
        return retrofit as Retrofit
    }
}