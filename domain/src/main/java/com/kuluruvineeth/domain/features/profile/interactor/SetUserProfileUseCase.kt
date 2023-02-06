package com.kuluruvineeth.domain.features.profile.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.features.profile.module.UserProfile
import com.kuluruvineeth.domain.features.profile.repo.ProfileRepository
import javax.inject.Inject

class SetUserProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) : UseCase<SetUserProfileUseCase.Params,Unit>(){

    override suspend fun operation(params: Params){
        repository.setUserProfile(params.userProfile)
    }

    data class Params(val userProfile: UserProfile)
}