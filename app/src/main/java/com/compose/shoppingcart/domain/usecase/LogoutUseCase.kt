package com.compose.shoppingcart.domain.usecase

import com.compose.shoppingcart.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.logout()
}
