package com.compose.shoppingcart.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.shoppingcart.domain.model.Product
import com.compose.shoppingcart.domain.usecase.AddToCartUseCase
import com.compose.shoppingcart.domain.usecase.GetCurrentUserUseCase
import com.compose.shoppingcart.domain.usecase.GetProductByIdUseCase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<FirebaseUser?>(getCurrentUserUseCase())
    val authState: StateFlow<FirebaseUser?> = _authState.asStateFlow()

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadProductById(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            getProductByIdUseCase(id)
                .onSuccess { _product.value = it }
                .onFailure { _error.value = it.message }
            _loading.value = false
        }
    }

    fun addToCart(product: Product, quantity: Int) {
        viewModelScope.launch {
            addToCartUseCase(product, quantity)
        }
    }
}
