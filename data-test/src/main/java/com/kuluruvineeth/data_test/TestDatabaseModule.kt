package com.kuluruvineeth.data_test

import android.content.Context
import androidx.room.Room
import com.kuluruvineeth.data.database.PoiDatabase
import com.kuluruvineeth.data.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PoiDatabase =
        Room.inMemoryDatabaseBuilder(
            context,
            PoiDatabase::class.java
        ).build()
}