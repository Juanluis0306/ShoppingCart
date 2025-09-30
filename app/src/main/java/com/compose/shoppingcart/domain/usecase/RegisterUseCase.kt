package com.compose.shoppingcart.domain.usecase

import com.compose.shoppingcart.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        repository.register(email, password)
}
