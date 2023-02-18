package com.kuluruvineeth.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kuluruvineeth.data.features.categories.dao.CategoriesDao
import com.kuluruvineeth.data.features.categories.model.CategoryEntity
import com.kuluruvineeth.data.features.poi.db.PoiDao
import com.kuluruvineeth.data.features.poi.model.PoiCommentEntity
import com.kuluruvineeth.data.features.poi.model.PoiEntity
import com.kuluruvineeth.data.features.poi.model.PoiFtsEntity
import com.kuluruvineeth.data.features.poi.model.PoiWithCategoriesCrossRef

@Database(
    entities = [
        CategoryEntity::class,
        PoiEntity::class,
        PoiFtsEntity::class,
        PoiWithCategoriesCrossRef::class,
        PoiCommentEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(InstantConverter::class)
abstract class PoiDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
    abstract fun poiDao(): PoiDao
}