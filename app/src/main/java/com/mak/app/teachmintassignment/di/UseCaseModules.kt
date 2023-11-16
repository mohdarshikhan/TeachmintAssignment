package com.mak.app.teachmintassignment.di

import android.content.Context
import com.mak.app.teachmintassignment.domain.repo.repository.RepoRepository
import com.mak.app.teachmintassignment.domain.repo.usecase.RepoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModules {

    @Singleton
    @Provides
    fun provideAuthUseCase(
        @ApplicationContext applicationContext: Context,
        repoRepository: RepoRepository
    ) = RepoUseCase(applicationContext, repoRepository)
}