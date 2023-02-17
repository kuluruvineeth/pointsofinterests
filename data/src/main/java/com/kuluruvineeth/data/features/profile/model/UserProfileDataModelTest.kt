package com.kuluruvineeth.data.features.profile.model

import com.kuluruvineeth.data.features.profile.datastore.UserProfileProto
import com.kuluruvineeth.domain.features.profile.module.UserProfile
import org.junit.Test
import kotlin.test.assertEquals

class UserProfileDataModelTest {

    @Test
    fun test_UserProfileDataModel_toDomain_function_returns_UserProfile_model_with_correct_fields(){

        val dataModel = UserProfileDataModel(
            token = "Token",
            name = "Name",
            email = "email",
            profileImage = "image"
        )

        val domainModel = dataModel.toDomain()

        assertEquals(dataModel.token, domainModel.authToken)
        assertEquals(dataModel.name, domainModel.name)
        assertEquals(dataModel.email, domainModel.email)
        assertEquals(dataModel.profileImage, domainModel.image)
    }

    @Test
    fun test_UserProfile_toDataModel_function_returns_UserProfileDataModel_model_with_correct_fields(){

        val domainModel = UserProfile(
            authToken = "Token",
            name = "Name",
            email = "email",
            image = "image"
        )

        val dataModel = domainModel.toDataModel()

        assertEquals(domainModel.authToken, dataModel.token)
        assertEquals(domainModel.name, dataModel.name)
        assertEquals(domainModel.email, dataModel.email)
        assertEquals(domainModel.image, dataModel.profileImage)
    }

    @Test
    fun test_UserProfileProto_toDataModel_function_returns_CategoryDataModel_model_with_correct_fields(){

        val userProfileProto = UserProfileProto.newBuilder().apply {
            authToken = "Token"
            name = "Name"
            email = "Email"
            profileImage = "Image"
        }.build()

        val dataModel = userProfileProto.toDataModel()

        assertEquals(dataModel.token, userProfileProto.authToken)
        assertEquals(dataModel.name, userProfileProto.name)
        assertEquals(dataModel.email, userProfileProto.email)
        assertEquals(dataModel.profileImage,userProfileProto.profileImage)
    }
}