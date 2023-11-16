package com.mak.app.teachmintassignment.di

import android.content.Context
import com.mak.app.teachmintassignment.utils.NetworkUtility
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilitiesModules {

    @Singleton
    @Provides
    fun provideNetworkUtility(
        @ApplicationContext applicationContext: Context,
    ) = NetworkUtility(applicationContext)
}