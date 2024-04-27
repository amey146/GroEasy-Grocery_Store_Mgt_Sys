package com.example.groeasy.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class], exportSchema = false, version = 6)
abstract class DatabaseHelper : RoomDatabase() {

    companion object {
        private const val DB_NAME = "product_db"

        @Volatile
        private var instance: DatabaseHelper? = null

        @Synchronized
        fun getDb(context: Context): DatabaseHelper {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): DatabaseHelper {
            return Room.databaseBuilder(
                context.applicationContext,
                DatabaseHelper::class.java,
                DB_NAME
            )
                .allowMainThreadQueries()
                .build()
        }
    }

    abstract fun productDao(): ProductDao
}
