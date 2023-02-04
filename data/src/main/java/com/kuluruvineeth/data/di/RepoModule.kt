package com.kuluruvineeth.data.di

import com.kuluruvineeth.data.features.categories.repository.CategoriesRepositoryImpl
import com.kuluruvineeth.domain.features.categories.repo.CategoriesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface RepoModule {

    @Binds
    @Singleton
    fun bindCategoriesRepo(impl: CategoriesRepositoryImpl): CategoriesRepository
}