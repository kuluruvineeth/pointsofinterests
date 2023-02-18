package com.kuluruvineeth.data_test

import com.kuluruvineeth.data.core.Local
import com.kuluruvineeth.data.core.Remote
import com.kuluruvineeth.data.di.DataSourceModule
import com.kuluruvineeth.data.features.categories.datasource.CategoriesDataSource
import com.kuluruvineeth.data.features.categories.datasource.CategoriesFakeRemoteDataSource
import com.kuluruvineeth.data.features.categories.datasource.CategoriesLocalDataSource
import com.kuluruvineeth.data.features.profile.datasource.ProfileDataSource
import com.kuluruvineeth.data.features.profile.datasource.ProfileLocalDataSource
import com.kuluruvineeth.data_test.doubles.TestLocalImageDataSource
import com.kuluruvineeth.data.features.poi.datasource.*
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataSourceModule::class]
)
interface TestDataSourceModule {

    @Binds
    @Local
    fun bindImageDataSource(impl: TestLocalImageDataSource): ImageDataSource

    @Binds
    @Local
    fun bindCategoryLocalDataSource(dataSource: CategoriesLocalDataSource): CategoriesDataSource

    @Binds
    @Remote
    fun bindCategoryRemoteDataSource(dataSource: CategoriesFakeRemoteDataSource): CategoriesDataSource

    @Binds
    @Local
    fun bindProfileLocalDataSource(dataSource: ProfileLocalDataSource): ProfileDataSource

    @Binds
    @Local
    fun bindPoiLocalDataSource(dataSource: PoiLocalDataSource): PoiDataSource

    @Binds
    @Remote
    fun bindWizardRemoteDataSource(dataSource: WizardRemoteDataSource): WizardDataSource
}