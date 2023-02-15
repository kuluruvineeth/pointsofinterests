package com.kuluruvineeth.data.di

import android.content.Context
import androidx.room.Room
import com.kuluruvineeth.data.database.PoiDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


private const val DB_NAME = "kuluruvineeth-poi-database"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PoiDatabase =
        Room.databaseBuilder(
            context,
            PoiDatabase::class.java,
            "kuluruvineeth-poi-database3")
            //.createFromAsset(DB_NAME)
            .build()
}