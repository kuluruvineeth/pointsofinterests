package com.kuluruvineeth.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kuluruvineeth.data.features.categories.dao.CategoriesDao
import com.kuluruvineeth.data.features.categories.model.CategoryEntity


@Database(
    entities = [CategoryEntity::class],
    version = 1,
    exportSchema = true
)
abstract class PoiDatabase : RoomDatabase(){
    abstract fun categoriesDao(): CategoriesDao
}