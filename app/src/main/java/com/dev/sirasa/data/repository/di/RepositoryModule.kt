package com.dev.sirasa.data.repository.di

import com.dev.sirasa.data.local.UserPreference
import com.dev.sirasa.data.remote.retrofit.ApiService
import com.dev.sirasa.data.repository.AuthRepository
import com.dev.sirasa.data.repository.BookingRepository
import com.dev.sirasa.data.repository.RoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule{
    @Provides
    @Singleton
    fun provideAuthRepository(
        userPreference: UserPreference,
        apiService: ApiService
    ) : AuthRepository{
        return AuthRepository(userPreference, apiService)
    }

    @Provides
    @Singleton
    fun provideBookingRepository(
        userPreference: UserPreference,
        apiService: ApiService
    ) : BookingRepository {
        return BookingRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideRoomRepository(
        userPreference: UserPreference,
        apiService: ApiService
    ) : RoomRepository{
        return RoomRepository(apiService)
    }
}