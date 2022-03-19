package com.jaquelinebruzasco.drinksforfun.di

import com.jaquelinebruzasco.drinksforfun.domain.remote.api.DrinksForFunApi
import com.jaquelinebruzasco.drinksforfun.domain.remote.model.ApiConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideServiceApi(retrofit: Retrofit): DrinksForFunApi {
        return retrofit.create(DrinksForFunApi::class.java)
    }
}