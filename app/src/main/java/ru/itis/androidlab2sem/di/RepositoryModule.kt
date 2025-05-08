package ru.itis.androidlab2sem.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.androidlab2sem.data.repository.CatRepositoryImpl
import ru.itis.androidlab2sem.domain.repository.CatRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCatRepository(
        impl: CatRepositoryImpl
    ): CatRepository
}