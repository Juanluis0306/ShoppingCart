package com.compose.shoppingcart.data.repository

import com.compose.shoppingcart.data.local.dao.CartDao
import com.compose.shoppingcart.data.local.entity.CartItemEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartRepositoryImplTest {

    private lateinit var cartDao: CartDao
    private lateinit var repository: CartRepositoryImpl

    @Before
    fun setup() {
        cartDao = mockk(relaxed = true)
        repository = CartRepositoryImpl(cartDao)
    }

    @Test
    fun `getCartItems devuelve items del dao`() = runTest {
        val fakeItems = listOf(
            CartItemEntity(
                productId = 1,
                title = "Test 1",
                price = 10.0,
                quantity = 1,
                thumbnail = "url"
            ),
            CartItemEntity(
                productId = 2,
                title = "Test 2",
                price = 20.0,
                quantity = 3,
                thumbnail = "url"
            )
        )
        coEvery { cartDao.getAllItems() } returns flowOf(fakeItems)

        val result = repository.getCartItems()

        result.collect { items ->
            assertEquals(2, items.size)
            assertEquals("Test 1", items[0].title)
        }
    }

    @Test
    fun `removeFromCart debe llamar deleteById`() = runTest {
        val entity = CartItemEntity(
            id = 1,
            productId = 2,
            title = "Test 2",
            price = 20.0,
            quantity = 3,
            thumbnail = "url"
        )

        repository.addToCart(entity)

        coEvery { cartDao.deleteById(any()) } returns Unit


        repository.removeFromCart(entity)

        coVerify { cartDao.deleteById(1) }
    }
}
