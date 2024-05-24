package com.example.storeapp.injection.modules

import com.example.data.RepositoryImpl
import com.example.domain.Repository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface BindModule {
    @Binds
    @Singleton
    fun bindRepository(repository: RepositoryImpl): Repository
}