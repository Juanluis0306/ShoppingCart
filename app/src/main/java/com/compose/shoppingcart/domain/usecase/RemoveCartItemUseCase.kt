package com.compose.shoppingcart.domain.usecase

import com.compose.shoppingcart.data.local.dao.CartDao
import javax.inject.Inject

class RemoveCartItemUseCase @Inject constructor(
    private val cartDao: CartDao
) {
    suspend operator fun invoke(id: Int) {
        cartDao.deleteById(id)
    }
}