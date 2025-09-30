package com.compose.shoppingcart.domain.usecase

import com.compose.shoppingcart.data.local.dao.CartDao
import javax.inject.Inject

class UpdateCartItemUseCase @Inject constructor(
    private val cartDao: CartDao
) {
    suspend operator fun invoke(id: Int, newQuantity: Int) {
        cartDao.updateQuantity(id, newQuantity)
    }
}