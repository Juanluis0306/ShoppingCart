package com.compose.shoppingcart.di

import android.content.Context
import androidx.room.Room
import com.compose.shoppingcart.data.local.AppDatabase
import com.compose.shoppingcart.data.local.dao.CartDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "shopping_cart_db"
        ).build()

    @Provides
    fun provideCartDao(db: AppDatabase): CartDao = db.cartDao()
}
