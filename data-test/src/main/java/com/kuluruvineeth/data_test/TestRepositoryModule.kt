package com.kuluruvineeth.data_test

import com.kuluruvineeth.data.di.RepoModule
import com.kuluruvineeth.data_test.doubles.TestCategoriesRepository
import com.kuluruvineeth.data_test.doubles.TestPoiRepository
import com.kuluruvineeth.data_test.doubles.TestProfileRepository
import com.kuluruvineeth.domain.features.categories.repo.CategoriesRepository
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import com.kuluruvineeth.domain.features.profile.repo.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepoModule::class]
)
interface TestRepositoryModule {

    @Binds
    @Singleton
    fun bindPoiRepository(impl: TestPoiRepository): PoiRepository

    @Binds
    @Singleton
    fun bindCategoriesRepository(impl: TestCategoriesRepository): CategoriesRepository

    @Binds
    @Singleton
    fun bindProfileRepository(impl: TestProfileRepository): ProfileRepository
}