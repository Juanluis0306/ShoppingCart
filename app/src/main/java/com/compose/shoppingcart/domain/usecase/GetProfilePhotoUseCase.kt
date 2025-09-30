package com.compose.shoppingcart.domain.usecase

import com.compose.shoppingcart.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfilePhotoUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<String?> = repository.getProfilePhoto()
}
