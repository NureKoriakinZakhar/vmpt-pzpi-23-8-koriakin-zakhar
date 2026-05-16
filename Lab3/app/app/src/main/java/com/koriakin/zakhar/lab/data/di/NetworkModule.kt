package com.koriakin.zakhar.lab.data.di

import com.google.gson.Gson
import com.koriakin.zakhar.lab.data.repository.GameRepositoryImpl
import com.koriakin.zakhar.lab.domain.repository.GameRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindGameRepository(impl: GameRepositoryImpl): GameRepository

    companion object {

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS)
            .writeTimeout(0, TimeUnit.SECONDS)
            .build()

        @Provides
        @Singleton
        fun provideGson(): Gson = Gson()
    }
}
