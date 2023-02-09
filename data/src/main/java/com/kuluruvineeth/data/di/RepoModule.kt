package com.kuluruvineeth.data.di

import com.kuluruvineeth.data.features.categories.repository.CategoriesRepositoryImpl
import com.kuluruvineeth.data.features.poi.repository.PoiRepositoryImpl
import com.kuluruvineeth.data.features.profile.repository.ProfileRepositoryImpl
import com.kuluruvineeth.domain.features.categories.repo.CategoriesRepository
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import com.kuluruvineeth.domain.features.profile.repo.ProfileRepository
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

    @Binds
    @Singleton
    fun bindProfileRepo(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    fun bindPoiRepo(impl: PoiRepositoryImpl): PoiRepository
}