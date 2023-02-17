package com.kuluruvineeth.data.features.categories.model

import android.graphics.Color
import com.kuluruvineeth.domain.features.categories.models.CategoryType
import org.junit.Test
import kotlin.test.assertEquals

class CategoryEntityTest {

    @Test
    fun test_CategoryDataModel_toEntity_function_returns_CategoryEntity_model_with_correct_fields(){

        val categoryDataModel = CategoryDataModel(
            id = 1,
            title = "Title",
            color = Color.WHITE,
            type = "GLOBAL",
            isMutable = false
        )

        val entity = categoryDataModel.toEntity()

        assertEquals(categoryDataModel.id, entity.id)
        assertEquals(categoryDataModel.title, entity.title)
        assertEquals(categoryDataModel.color, entity.color)
        assertEquals(categoryDataModel.type, entity.type)
        assertEquals(categoryDataModel.isMutable, entity.isMutable)
    }

    @Test
    fun test_CategoryEntity_toDataModel_function_returns_CategoryDataModel_model_with_correct_fields(){

        val entity = CategoryEntity(
            title = "Title",
            color = Color.WHITE,
            type = CategoryType.GLOBAL.name,
            isMutable = false
        ).apply {
            id = 1
        }

        val dataModel = entity.toDataModel()

        assertEquals(entity.id, dataModel.id)
        assertEquals(entity.title, dataModel.title)
        assertEquals(entity.color, dataModel.color)
        assertEquals(entity.type, dataModel.type)
        assertEquals(entity.isMutable, dataModel.isMutable)
    }
}