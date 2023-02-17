package com.kuluruvineeth.data.features.categories.model

import android.graphics.Color
import com.kuluruvineeth.data.core.UNSPECIFIED_ID
import com.kuluruvineeth.domain.features.categories.models.Category
import com.kuluruvineeth.domain.features.categories.models.CategoryType
import com.kuluruvineeth.domain.features.categories.models.CreateCategoryPayload
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class CategoryDataModelTest {

    @Test
    fun test_CategoryDataModel_toDomain_function_returns_Category_model_with_correct_fields(){

        val categoryDataModel = CategoryDataModel(
            id = 1,
            title = "Title",
            color = Color.WHITE,
            type = "GLOBAL",
            isMutable = false
        )

        val domainModel = categoryDataModel.toDomain()

        assertEquals(categoryDataModel.id, domainModel.id.toInt())
        assertEquals(categoryDataModel.title, domainModel.title)
        assertEquals(categoryDataModel.color, domainModel.color)
        assertEquals(categoryDataModel.type, domainModel.categoryType.name)
        assertEquals(categoryDataModel.isMutable, domainModel.isMutable)
    }

    @Test
    fun test_Category_toDataModel_function_returns_CategoryDataModel_model_with_correct_fields(){

        val domainModel = Category(
            id = "1",
            title = "Title",
            color = Color.WHITE,
            categoryType = CategoryType.GLOBAL,
            isMutable = false
        )

        val dataModel = domainModel.toDataModel()

        assertEquals(domainModel.id.toInt(), dataModel.id)
        assertEquals(domainModel.title, dataModel.title)
        assertEquals(domainModel.color, dataModel.color)
        assertEquals(domainModel.categoryType.name, dataModel.type)
        assertEquals(domainModel.isMutable, dataModel.isMutable)
    }


    @Test
    fun test_CreateCategory_toDataModel_function_returns_CategoryDataModel_model_with_correct_fields(){
        val payload = CreateCategoryPayload(
            title = "Title",
            color = Color.WHITE
        )

        val dataModel = payload.toDataModel()

        assertEquals(dataModel.id, UNSPECIFIED_ID)
        assertEquals(dataModel.title, payload.title)
        assertEquals(dataModel.color, payload.color)
        assertEquals(dataModel.type, CategoryType.PERSONAL.name)
        assertTrue(dataModel.isMutable)
    }
}