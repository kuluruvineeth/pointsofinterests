package com.kuluruvineeth.data_test

import com.kuluruvineeth.domain.di.DispatchersModule
import com.kuluruvineeth.domain.di.UseCaseDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatchersModule::class]
)
object TestDispatchersModule {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    @Singleton
    @UseCaseDispatcher
    fun provideDispatcher(): CoroutineDispatcher = UnconfinedTestDispatcher()
}