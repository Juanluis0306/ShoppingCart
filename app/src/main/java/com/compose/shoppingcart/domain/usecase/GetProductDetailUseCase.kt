package com.compose.shoppingcart.domain.usecase

import com.compose.shoppingcart.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductDetailUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: Int) = repository.getProductDetail(id)
}
