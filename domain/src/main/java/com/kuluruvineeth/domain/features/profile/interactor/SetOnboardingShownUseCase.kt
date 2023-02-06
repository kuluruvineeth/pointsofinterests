package com.kuluruvineeth.domain.features.profile.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.features.profile.repo.ProfileRepository
import javax.inject.Inject

class SetOnboardingShownUseCase @Inject constructor(
    private val repository: ProfileRepository
) : UseCase<Unit,Unit>(){

    override suspend fun operation(params: Unit){
        repository.setShowOnBoarding(false)
    }
}