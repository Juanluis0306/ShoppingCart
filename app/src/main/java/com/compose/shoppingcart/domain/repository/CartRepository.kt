package com.compose.shoppingcart.domain.repository

import com.compose.shoppingcart.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItemEntity>>
    suspend fun addToCart(item: CartItemEntity)
    suspend fun removeFromCart(item: CartItemEntity)
    suspend fun clearCart()
}
