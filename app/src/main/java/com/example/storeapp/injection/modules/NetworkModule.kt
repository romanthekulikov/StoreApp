package com.example.storeapp.injection.modules

import com.example.data.api.ProductApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

const val BASE_URL = "https://fakestoreapi.com/"

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideProductApiService(): ProductApiService {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL).build()
        return retrofit.create(ProductApiService::class.java)
    }
}