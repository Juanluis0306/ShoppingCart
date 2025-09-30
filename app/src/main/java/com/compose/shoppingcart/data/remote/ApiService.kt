package com.compose.shoppingcart.data.remote

import com.compose.shoppingcart.domain.model.Product
import com.compose.shoppingcart.domain.model.ProductsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 40,
        @Query("skip") skip: Int = 0
    ): ProductsResponse

    @GET("products/{id}")
    suspend fun getProductDetail(@Path("id") id: Int): Product
}