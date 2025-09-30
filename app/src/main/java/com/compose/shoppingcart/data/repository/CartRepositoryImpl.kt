package com.compose.shoppingcart.data.repository

import com.compose.shoppingcart.data.local.dao.CartDao
import com.compose.shoppingcart.data.local.entity.CartItemEntity
import com.compose.shoppingcart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {

    override fun getCartItems(): Flow<List<CartItemEntity>> = cartDao.getAllItems()

    override suspend fun addToCart(item: CartItemEntity) {
        cartDao.insert(item)
    }

    override suspend fun removeFromCart(item: CartItemEntity) {
        cartDao.deleteById(item.id)
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }
}
