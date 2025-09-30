package com.compose.shoppingcart.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.shoppingcart.domain.model.ProductsResponse
import com.compose.shoppingcart.domain.usecase.GetCurrentUserUseCase
import com.compose.shoppingcart.domain.usecase.GetProductsUseCase
import com.compose.shoppingcart.domain.usecase.GetProfilePhotoUseCase
import com.compose.shoppingcart.domain.usecase.LogoutUseCase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val getProfilePhotoUseCase: GetProfilePhotoUseCase
) : ViewModel() {

    private val _photoUri = MutableStateFlow<String?>(null)
    val photoUri: StateFlow<String?> = _photoUri

    init {
        viewModelScope.launch {
            getProfilePhotoUseCase().collect { uri ->
                _photoUri.value = uri
            }
        }
    }

    private val _products = MutableStateFlow<ProductsResponse?>(null)
    val product = _products.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _authState = MutableStateFlow<FirebaseUser?>(getCurrentUserUseCase())
    val authState: StateFlow<FirebaseUser?> = _authState.asStateFlow()

    init {
        loadProducts()
    }

    fun logout(onSuccess: () -> Unit, onError: (Throwable?) -> Unit) {
        try {
            logoutUseCase()
            _authState.value = null
            onSuccess()
        } catch (e: Exception) {
            onError(e)
        }
    }

    fun loadProducts(
        onSuccess: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        viewModelScope.launch {
            _loading.value = true
            val result = getProductsUseCase()
            _loading.value = false
            result.onSuccess {
                _products.value = it
                onSuccess?.invoke()
            }.onFailure {
                _error.value = it.message
                onError?.invoke(it.message ?: "Error desconocido")
            }
        }
    }
}