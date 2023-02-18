package com.kuluruvineeth.data.features.poi.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kuluruvineeth.data.features.poi.model.PoiCommentEntity
import com.kuluruvineeth.data.features.poi.model.PoiEntity
import com.kuluruvineeth.data.features.poi.model.PoiWithCategoriesCrossRef
import com.kuluruvineeth.data.features.poi.model.PoiWithCategoriesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PoiDao {

    @Transaction
    @Query(
        value = """
            SELECT * FROM table_poi 
            ORDER BY
            CASE WHEN :column = 'creation_date_time' THEN creation_date_time END DESC,
            CASE WHEN :column = 'severity' THEN severity END ASC,
            CASE WHEN :column = 'title' THEN title END ASC
            """)
    fun getPoiList(column: String): Flow<List<PoiWithCategoriesEntity>>

    @Transaction
    @Query(
        value = """
            SELECT * FROM table_poi
            JOIN table_poi_fts ON table_poi.id = table_poi_fts.rowid
            WHERE table_poi_fts MATCH :query
        """
    )
    suspend fun searchPoi(query: String): List<PoiWithCategoriesEntity>

    @Query(value = "SELECT DISTINCT category_id FROM table_poi_to_category")
    fun getUsedCategoriesIds(): Flow<List<Int>>

    @Transaction
    suspend fun insertPoiTransaction(entity: PoiEntity, categories: List<Int>){
        val poiId = insertPoi(entity)
        val crossRefs = categories.map { categoryId ->
            PoiWithCategoriesCrossRef(
                poiEntityId = poiId.toInt(),
                categoryEntityId = categoryId
            )
        }
        insertOrIgnoreCategoryCrossRefEntities(crossRefs)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoi(entity: PoiEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreCategoryCrossRefEntities(entities: List<PoiWithCategoriesCrossRef>)

    @Transaction
    @Query(value = "SELECT * FROM table_poi WHERE id = :id")
    suspend fun getPoi(id: Int): PoiWithCategoriesEntity

    @Query(value = "DELETE FROM table_poi WHERE id = :id")
    suspend fun deletePoi(id: Int)

    @Query(value = "DELETE FROM table_poi_to_category WHERE poi_id IN (:ids)")
    suspend fun deleteUsedCategories(ids: List<Int>)

    @Query(value = "UPDATE table_poi SET viewed = :isViewed WHERE id = :id")
    suspend fun updatePoiViewed(id: Int, isViewed: Boolean)

    @Transaction
    suspend fun deletePoiOlderThen(thresholdDate: Long): List<PoiEntity>{

        val poiToDelete = getPoiIdsOlderThen(thresholdDate)
        val idsToDelete = poiToDelete.map{it.id}
        deletePoiList(idsToDelete)
        deleteUsedCategories(idsToDelete)
        return poiToDelete
    }

    @Query(value = "SELECT * FROM table_poi WHERE creation_date_time < :thresholdDate")
    suspend fun getPoiIdsOlderThen(thresholdDate: Long): List<PoiEntity>

    @Query(value = "DELETE FROM table_poi WHERE id IN (:ids)")
    suspend fun deletePoiList(ids: List<Int>)

    @Transaction
    suspend fun insertCommentTransaction(entity: PoiCommentEntity){
        insertComment(entity)
        val count = commentsCount(entity.parentId)
        updatePoiCommentsCount(entity.parentId, count)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(entity: PoiCommentEntity)

    @Query(value = "UPDATE table_poi SET comments_count = :count WHERE id = :id")
    suspend fun updatePoiCommentsCount(id: Int, count: Int)

    @Query(value = "SELECT COUNT(*) FROM table_poi_comments WHERE parent_id = :parentId")
    suspend fun commentsCount(parentId: Int): Int

    @Query(value = "SELECT * FROM table_poi_comments WHERE parent_id = :parentId")
    fun getComments(parentId: Int): Flow<List<PoiCommentEntity>>

    @Query(value = "DELETE FROM table_poi_comments WHERE id = :id")
    suspend fun deleteComment(id: Int)

    @Query(value = " DELETE FROM table_poi_comments WHERE parent_id = :parentId")
    suspend fun deleteCommentsForParent(parentId: Int)

    @Query(value = " DELETE FROM table_poi_comments WHERE parent_id IN (:parentIds)")
    suspend fun deleteCommentsForParents(parentIds: List<Int>)
}