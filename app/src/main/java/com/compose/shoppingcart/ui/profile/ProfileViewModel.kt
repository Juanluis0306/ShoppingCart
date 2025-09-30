package com.compose.shoppingcart.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.shoppingcart.domain.usecase.GetProfilePhotoUseCase
import com.compose.shoppingcart.domain.usecase.LogoutUseCase
import com.compose.shoppingcart.domain.usecase.SaveProfilePhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfilePhotoUseCase: GetProfilePhotoUseCase,
    private val saveProfilePhotoUseCase: SaveProfilePhotoUseCase,
    private val logoutUseCase: LogoutUseCase,
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

    fun savePhoto(uri: Uri) {
        viewModelScope.launch {
            saveProfilePhotoUseCase(uri.toString())
        }
    }
    fun logout(onSuccess: () -> Unit, onError: (Throwable?) -> Unit) {
        try {
            logoutUseCase()
            onSuccess()
        } catch (e: Exception) {
            onError(e)
        }
    }

}
