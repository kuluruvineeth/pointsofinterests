package com.kuluruvineeth.domain.features.profile.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.di.UseCaseDispatcher
import com.kuluruvineeth.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteUserProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : UseCase<Unit, Unit>(dispatcher) {
    override suspend fun operation(params: Unit) {
        repository.deleteUserProfile()
    }
}