package com.compose.shoppingcart.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.shoppingcart.data.local.entity.CartItemEntity
import com.compose.shoppingcart.domain.usecase.GetCartItemsUseCase
import com.compose.shoppingcart.domain.usecase.RemoveCartItemUseCase
import com.compose.shoppingcart.domain.usecase.UpdateCartItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    getCartItemsUseCase: GetCartItemsUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase,
    private val removeCartItemUseCase: RemoveCartItemUseCase
) : ViewModel() {

    val cartItems: StateFlow<List<CartItemEntity>> =
        getCartItemsUseCase()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateQuantity(id: Int, newQuantity: Int) {
        viewModelScope.launch {
            updateCartItemUseCase(id, newQuantity)
        }
    }

    fun removeFromCart(id: Int) {
        viewModelScope.launch {
            removeCartItemUseCase(id)
        }
    }
}
