package com.compose.shoppingcart.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.shoppingcart.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun register(
        email: String, password: String,
        onSuccess: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) = viewModelScope.launch {
        _loading.value = true
        val result = registerUseCase(email, password)
        _loading.value = false
        result.onSuccess {
            onSuccess?.invoke()
        }.onFailure {
            _error.value = it.message
            onError?.invoke(it.message ?: "Error desconocido")
        }
    }
}