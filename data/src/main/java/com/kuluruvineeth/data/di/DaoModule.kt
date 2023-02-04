package com.kuluruvineeth.data.di

import com.kuluruvineeth.data.database.PoiDatabase
import com.kuluruvineeth.data.features.categories.dao.CategoriesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideCategoriesDao(database: PoiDatabase): CategoriesDao =
        database.categoriesDao()
}