package com.kuluruvineeth.pointsofinterests.features.poi.viewmodel

import androidx.lifecycle.ViewModel
import com.kuluruvineeth.domain.features.poi.interactor.CreatePoiUseCase
import com.kuluruvineeth.domain.features.poi.interactor.GetWizardSuggestionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PoiViewModel @Inject constructor(
    private val getWizardSuggestionUseCase: GetWizardSuggestionUseCase,
    private val createPoiUseCase: CreatePoiUseCase
) : ViewModel(){


}