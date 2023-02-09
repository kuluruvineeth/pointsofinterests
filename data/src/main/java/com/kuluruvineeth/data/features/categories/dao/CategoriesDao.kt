package com.kuluruvineeth.data.features.categories.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kuluruvineeth.data.features.categories.model.CategoryEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CategoriesDao {

    @Query(value = "SELECT * FROM table_categories")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Query(value = "SELECT * FROM table_categories WHERE type = :type")
    fun getCategories(type: String): Flow<List<CategoryEntity>>

    @Query(value = "SELECT * FROM table_categories WHERE id IN (:idList)")
    fun getCategories(idList: List<Int>): Flow<List<CategoryEntity>>

    @Query(value = "SELECT * FROM table_categories WHERE id = :categoryId")
    suspend fun getCategory(categoryId: Int): CategoryEntity

    @Query("SELECT COUNT(*) FROM table_categories")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categoryEntity: List<CategoryEntity>) : List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(categoryEntity: CategoryEntity): Long

    @Update
    suspend fun updateCategory(categoryEntity: CategoryEntity)

    @Query(value = "DELETE FROM table_categories WHERE id = :categoryId")
    suspend fun deleteCategory(categoryId: Int)
}