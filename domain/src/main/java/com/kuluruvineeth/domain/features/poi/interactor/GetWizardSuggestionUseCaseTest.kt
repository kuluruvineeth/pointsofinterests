package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.MockitoHelper.anyNonNull
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.MockitoHelper.mock
import com.kuluruvineeth.domain.MockitoHelper.whenever
import com.kuluruvineeth.domain.features.poi.models.WizardSuggestion
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class GetWizardSuggestionUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: GetWizardSuggestionUseCase

    @Before
    fun setup(){
        SUT = GetWizardSuggestionUseCase(repository)
    }

    @Test
    fun test_GetWizardSuggestionUseCase_invokes_getWizardSuggestion_repo_function() = runTest {

        val contentUrl = "https://something.com"
        val suggestion = mock<WizardSuggestion>()

        whenever(repository.getWizardSuggestion(anyNonNull())).thenReturn(suggestion)
        val result = SUT.invoke(GetWizardSuggestionUseCase.Params(contentUrl))
        val captor = argumentCaptor<String>()
        verify(repository, Mockito.times(1)).getWizardSuggestion(capture(captor))
        assertEquals(contentUrl, captor.value)
        assertEquals(suggestion, result)
    }

    @Test(expected = Throwable::class)
    fun test_GetWizardSuggestionUseCase_throws_exception_when_getWizardSuggestion_throws_exception() = runTest {

        val contentUrl = "https://something.com"

        whenever(repository.getWizardSuggestion(anyNonNull())).thenThrow(
            IllegalStateException()
        )
        SUT.invoke(GetWizardSuggestionUseCase.Params(contentUrl))
    }
}