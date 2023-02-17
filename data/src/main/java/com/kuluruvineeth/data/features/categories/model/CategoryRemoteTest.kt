package com.kuluruvineeth.data.features.categories.model

import android.graphics.Color
import androidx.core.graphics.toColorInt
import com.kuluruvineeth.data.core.UNSPECIFIED_ID
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@RunWith(MockitoJUnitRunner::class)
class CategoryRemoteTest {

    @Test
    fun test_CategoryRemote_toDataModel_function_returns_CategoryDataModel_model_with_correct_fields(){

        val colorMock: MockedStatic<Color> = Mockito.mockStatic(Color::class.java)
        colorMock.`when`<Int>{Color.parseColor("#FFFFFF")}.thenReturn(Color.WHITE)

        val remote = CategoryRemote(
            title = "Title",
            color = "#FFFFFF",
            type = "GLOBAL"
        )

        val dataModel = remote.toDataModel()

        assertEquals(UNSPECIFIED_ID, dataModel.id)
        assertEquals(remote.title, dataModel.title)
        assertEquals(remote.color.toColorInt(), dataModel.color)
        assertEquals(remote.type, dataModel.type)
        assertFalse(dataModel.isMutable)
    }
}