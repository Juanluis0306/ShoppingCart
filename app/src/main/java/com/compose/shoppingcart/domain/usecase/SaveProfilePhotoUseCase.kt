package com.compose.shoppingcart.domain.usecase

import com.compose.shoppingcart.domain.repository.ProfileRepository
import javax.inject.Inject

class SaveProfilePhotoUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(uri: String) {
        repository.saveProfilePhoto(uri)
    }
}