package com.kuluruvineeth.data.features.poi.datasource

import android.util.Log
import com.kuluruvineeth.data.features.poi.api.WizardServiceApi
import com.kuluruvineeth.data.features.poi.model.WizardSuggestionDataModel
import javax.inject.Inject

class WizardRemoteDataSource @Inject constructor(
    private val api: WizardServiceApi
) {

    suspend fun getWizardSuggestion(url: String): WizardSuggestionDataModel{
        val responseBody = api.getUrlContent(url)
        val contentType = responseBody.contentType()?.type
        Log.d("AAA","Content Type $contentType")
        return if (contentType?.contains("image") == true || contentType == "binary"){
            WizardSuggestionDataModel(contentUrl = url, imageUrl = url)
        }else{
            WizardSuggestionDataModel(contentUrl = url, title = "Test",body = "Test body")
        }
    }
}