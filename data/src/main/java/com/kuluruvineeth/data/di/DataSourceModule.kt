package com.kuluruvineeth.data.di

import com.kuluruvineeth.data.core.Local
import com.kuluruvineeth.data.core.Remote
import com.kuluruvineeth.data.features.categories.datasource.CategoriesDataSource
import com.kuluruvineeth.data.features.categories.datasource.CategoriesFakeRemoteDataSource
import com.kuluruvineeth.data.features.categories.datasource.CategoriesLocalDataSource
import com.kuluruvineeth.data.features.poi.datasource.*
import com.kuluruvineeth.data.features.profile.datasource.ProfileDataSource
import com.kuluruvineeth.data.features.profile.datasource.ProfileLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

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
    @Local
    fun bindLocalImageDataSource(dataSource: LocalImageDataSource): ImageDataSource

    @Binds
    @Remote
    fun bindWizardRemoteDataSource(dataSource: WizardRemoteDataSource): WizardDataSource
}