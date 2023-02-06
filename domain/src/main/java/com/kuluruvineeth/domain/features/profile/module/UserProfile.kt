package com.kuluruvineeth.domain.features.profile.module

data class UserProfile(
    val authToken: String?,
    val name: String?,
    val email: String?,
    val image: String?
){
    val isAuthorized = authToken.isNullOrEmpty().not()
}
