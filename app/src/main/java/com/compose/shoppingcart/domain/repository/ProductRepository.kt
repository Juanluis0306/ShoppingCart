package com.compose.shoppingcart.domain.repository

import com.compose.shoppingcart.domain.model.Product
import com.compose.shoppingcart.domain.model.ProductsResponse

interface ProductRepository {
    suspend fun getProducts(): Result<ProductsResponse>
    suspend fun getProductDetail(id: Int): Result<Product>
}
