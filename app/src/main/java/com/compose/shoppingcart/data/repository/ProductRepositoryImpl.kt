package com.compose.shoppingcart.data.repository

import com.compose.shoppingcart.data.remote.ApiService
import com.compose.shoppingcart.domain.model.Product
import com.compose.shoppingcart.domain.model.ProductsResponse
import com.compose.shoppingcart.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ProductRepository {

    override suspend fun getProducts(): Result<ProductsResponse> {
        return try {
            Result.success(api.getProducts())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductDetail(id: Int): Result<Product> {
        return try {
            Result.success(api.getProductDetail(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}