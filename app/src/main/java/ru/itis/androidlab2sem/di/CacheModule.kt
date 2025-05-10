package ru.itis.androidlab2sem.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import ru.itis.androidlab2sem.domain.cache.CatCache

@Module
@InstallIn(ViewModelComponent::class)
object CacheModule {
    @Provides
    fun provideCacheDuration(): Long = 1 // Значение по умолчанию 5 минут

    @Provides
    @ViewModelScoped
    fun provideCatCache(duration: Long): CatCache {
        return CatCache(duration)
    }
}