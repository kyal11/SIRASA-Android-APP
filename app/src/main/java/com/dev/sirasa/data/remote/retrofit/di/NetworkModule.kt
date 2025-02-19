package com.dev.sirasa.data.remote.retrofit.di

import com.dev.sirasa.data.local.UserPreference
import com.dev.sirasa.data.remote.retrofit.ApiConfig
import com.dev.sirasa.data.remote.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideApiService(userPreference: UserPreference): ApiService {
        return ApiConfig.getApiService(userPreference)
    }
}