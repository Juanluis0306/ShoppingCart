package com.compose.shoppingcart.domain.usecase

import com.compose.shoppingcart.domain.model.Product
import com.compose.shoppingcart.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: Int): Result<Product> {
        return repository.getProductDetail(id)
    }
}