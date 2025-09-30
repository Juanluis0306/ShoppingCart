package com.compose.shoppingcart.domain.usecase

import com.compose.shoppingcart.data.local.dao.CartDao
import com.compose.shoppingcart.data.local.entity.CartItemEntity
import com.compose.shoppingcart.domain.model.Product
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartDao: CartDao
) {
    suspend operator fun invoke(product: Product, quantity: Int) {
        val existingItem = cartDao.getItemByProductId(product.id ?: 0)

        if (existingItem != null) {
            val newQuantity = existingItem.quantity + quantity
            cartDao.updateQuantity(existingItem.id, newQuantity)
        } else {
            val item = CartItemEntity(
                productId = product.id ?: 0,
                title = product.title ?: "",
                price = product.price ?: 0.0,
                quantity = quantity,
                thumbnail = product.thumbnail ?: ""
            )
            cartDao.insert(item)
        }
    }
}
