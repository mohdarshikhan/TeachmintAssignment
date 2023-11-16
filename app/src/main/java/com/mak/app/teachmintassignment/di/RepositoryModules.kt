package com.mak.app.teachmintassignment.di

import com.mak.app.teachmintassignment.utils.NetworkUtility
import com.mak.app.teachmintassignment.data.remote.AppApis
import com.mak.app.teachmintassignment.data.repositoryImpl.RepoRepositoryImpl
import com.mak.app.teachmintassignment.domain.repo.repository.RepoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModules {

    @Singleton
    @Provides
    fun provideAuthRepository(networkUtility: NetworkUtility, appApis: AppApis): RepoRepository =
        RepoRepositoryImpl(networkUtility, appApis)
}