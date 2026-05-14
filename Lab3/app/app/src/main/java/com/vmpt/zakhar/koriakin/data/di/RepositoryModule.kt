package com.vmpt.zakhar.koriakin.data.di

import com.vmpt.zakhar.koriakin.data.repository.MatchRepositoryImpl
import com.vmpt.zakhar.koriakin.domain.repository.MatchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMatchRepository(impl: MatchRepositoryImpl): MatchRepository
}
