package com.compose.shoppingcart.domain.repository

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfilePhoto(): Flow<String?>
    suspend fun saveProfilePhoto(uri: String)
}