package com.example.charlesschwab.di

import com.example.charlesschwab.data.repository.MockStockRepository
import com.example.charlesschwab.domain.repository.StockRepository
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
    abstract fun bindStockRepository(
        mockStockRepository: MockStockRepository
    ): StockRepository
}
