package com.compose.shoppingcart.domain.usecase

import com.compose.shoppingcart.data.local.dao.CartDao
import com.compose.shoppingcart.data.local.entity.CartItemEntity
import com.compose.shoppingcart.domain.model.Product
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddToCartUseCaseTest {

    private lateinit var cartDao: CartDao
    private lateinit var addToCartUseCase: AddToCartUseCase

    @Before
    fun setup() {
        cartDao = mockk(relaxed = true)
        addToCartUseCase = AddToCartUseCase(cartDao)
    }

    @Test
    fun `cuando agrego un producto debe llamar a insert en el dao`() = runTest {
        val product = Product(
            id = 1,
            title = "Producto Test",
            price = 100.0,
            thumbnail = "url"
        )

        coEvery { cartDao.getItemByProductId(1) } returns null
        coEvery { cartDao.insert(any()) } returns Unit

        addToCartUseCase(product, 2)

        coVerify {
            cartDao.insert(match {
                it.productId == 1 &&
                        it.title == "Producto Test" &&
                        it.price == 100.0 &&
                        it.quantity == 2 &&
                        it.thumbnail == "url"
            })
        }
    }
}

