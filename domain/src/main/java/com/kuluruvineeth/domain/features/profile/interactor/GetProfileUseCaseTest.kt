package com.kuluruvineeth.domain.features.profile.interactor

import com.kuluruvineeth.domain.MockitoHelper.mock
import com.kuluruvineeth.domain.MockitoHelper.whenever
import com.kuluruvineeth.domain.features.profile.module.UserProfile
import com.kuluruvineeth.domain.features.profile.module.UserSettings
import com.kuluruvineeth.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.flow.*
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
class GetProfileUseCaseTest {

    @Mock
    private lateinit var repository: ProfileRepository

    private lateinit var SUT: GetProfileUseCase

    @Before
    fun setup(){
        SUT = GetProfileUseCase(repository)
    }


    @Test
    fun test_GetProfileUseCase_invokes_getUserProfile_and_getUserSettings_repo_functions() = runTest {
        val mockProfileResult = mock<UserProfile>()
        val mockUserSettings = mock<UserSettings>()

        whenever(repository.getUserProfile()).thenReturn(flowOf(mockProfileResult))

        whenever(repository.getUserSetting()).thenReturn(flowOf(mockUserSettings))
        val result = SUT.invoke(Unit).first()
        verify(repository,Mockito.times(1)).getUserProfile()
        verify(repository,Mockito.times(1)).getUserSetting()
        assertEquals(mockProfileResult,result!!.userProfile)
        assertEquals(mockUserSettings,result!!.userSettings)
    }


    @Test
    fun test_GetProfileUseCase_emits_exception_when_flow_is_getUserProfile_throws_exception() = runTest {
        val mockUserSettings = mock<UserSettings>()

        whenever(repository.getUserSetting()).thenReturn(flowOf(mockUserSettings))
        whenever(repository.getUserProfile()).thenReturn(flow { throw IllegalStateException() })
        var exception: Throwable = mock()

        SUT.invoke(Unit).catch { exception = it }.toList()
        assertEquals(exception::class.java,IllegalStateException::class.java)
    }


    @Test
    fun test_get_profile_use_case_emits_exception_when_flow_in_getUserSettings_throws_exception() = runTest {
        val mockProfileResult = mock<UserProfile>()

        whenever(repository.getUserProfile()).thenReturn(flowOf(mockProfileResult))
        whenever(repository.getUserSetting()).thenReturn(flow { throw IllegalStateException() })
        var exception = Throwable()
        SUT.invoke(Unit).catch { exception = it }.toList()
        assertEquals(exception::class.java,IllegalStateException::class.java)
    }

}