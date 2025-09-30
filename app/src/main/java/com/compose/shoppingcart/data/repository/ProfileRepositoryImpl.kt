package com.compose.shoppingcart.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.compose.shoppingcart.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences
import com.compose.shoppingcart.BuildConfig
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ProfileRepository {

    companion object {
        private val PROFILE_PHOTO_URI = stringPreferencesKey(BuildConfig.STRING_KEY)
    }

    override fun getProfilePhoto(): Flow<String?> {
        return dataStore.data.map { prefs -> prefs[PROFILE_PHOTO_URI] }
    }

    override suspend fun saveProfilePhoto(uri: String) {
        dataStore.edit { prefs ->
            prefs[PROFILE_PHOTO_URI] = uri
        }
    }
}
