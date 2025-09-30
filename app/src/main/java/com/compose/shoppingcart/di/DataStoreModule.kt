package com.compose.shoppingcart.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.compose.shoppingcart.BuildConfig
import com.compose.shoppingcart.data.repository.ProfileRepositoryImpl
import com.compose.shoppingcart.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create {
            context.dataStoreFile(BuildConfig.DATASTORE)
        }

    @Provides
    @Singleton
    fun provideProfileRepository(dataStore: DataStore<Preferences>): ProfileRepository =
        ProfileRepositoryImpl(dataStore)
}