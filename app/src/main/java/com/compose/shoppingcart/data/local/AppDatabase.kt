package com.compose.shoppingcart.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.compose.shoppingcart.data.local.dao.CartDao
import com.compose.shoppingcart.data.local.entity.CartItemEntity

@Database(entities = [CartItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}