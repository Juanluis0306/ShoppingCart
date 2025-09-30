package com.compose.shoppingcart.domain.usecase

import com.compose.shoppingcart.data.local.dao.CartDao
import com.compose.shoppingcart.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartItemsUseCase @Inject constructor(
    private val cartDao: CartDao
) {
    operator fun invoke(): Flow<List<CartItemEntity>> = cartDao.getAllItems()
}
