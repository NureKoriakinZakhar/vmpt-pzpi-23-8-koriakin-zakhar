package com.vmpt.zakhar.koriakin.data.di

import android.content.Context
import androidx.room.Room
import com.vmpt.zakhar.koriakin.data.local.AppDatabase
import com.vmpt.zakhar.koriakin.data.local.dao.MatchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "tic_tac_toe.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMatchDao(db: AppDatabase): MatchDao = db.matchDao()
}
