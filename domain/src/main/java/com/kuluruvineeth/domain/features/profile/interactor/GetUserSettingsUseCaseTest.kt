package com.kuluruvineeth.domain.features.profile.interactor

import com.kuluruvineeth.domain.MockitoHelper
import com.kuluruvineeth.domain.MockitoHelper.whenever
import com.kuluruvineeth.domain.features.profile.module.UserSettings
import com.kuluruvineeth.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals


@RunWith(MockitoJUnitRunner::class)
class GetUserSettingsUseCaseTest {

    @Mock
    private lateinit var repository: ProfileRepository

    private lateinit var SUT: GetUserSettingsUseCase

    @Before
    fun setup(){
        SUT = GetUserSettingsUseCase(repository)
    }

    @Test
    fun test_GetUserSettingsUseCase_invokes_getUserSettings_repo_function() =
        runTest {

            val mockUserSettings = MockitoHelper.mock<UserSettings>()

            whenever(repository.getUserSetting()).thenReturn(flowOf(mockUserSettings))
            val result = SUT.invoke(Unit).first()
            Mockito.verify(repository,Mockito.times(1)).getUserSetting()
            assertEquals(mockUserSettings,result)
        }

    @Test
    fun test_GetUserSettingsUseCase_emits_exception_when_flow_in_getUserSettings_throws_exception() = runTest {

        whenever(repository.getUserSetting()).thenReturn(flow { throw IllegalStateException() })
        var exception = Throwable()
        SUT.invoke(Unit).catch { exception = it }.toList()
        assertEquals(exception::class.java,IllegalStateException::class.java)
    }
}