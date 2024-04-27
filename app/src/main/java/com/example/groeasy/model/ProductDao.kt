package com.example.groeasy.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface ProductDao {

    @Upsert
    suspend fun upsertProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Query("SELECT * FROM products ORDER BY id DESC LIMIT 10")
    fun getProductOrderId(): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE category = :category")
    fun getProductsByCategory(category: String?): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE expiry BETWEEN :todayInMillis AND :nextInMillis")
    fun getDatesWithinNextDays(todayInMillis: Long, nextInMillis: Long): LiveData<List<Product>>
    @Query("SELECT * FROM products WHERE expiry BETWEEN :todayInMillis AND :nextInMillis")
    fun getNotifiedExpiry(todayInMillis: Long, nextInMillis: Long): List<Product>
    @Query("SELECT * FROM products WHERE name LIKE '%' || :searchText || '%' COLLATE NOCASE OR brand LIKE '%' || :searchText || '%' COLLATE NOCASE")
    fun searchProducts(searchText: String): List<Product>

    @Query("SELECT * FROM products ORDER BY id DESC LIMIT 4")
    fun getProductSearchEmpty(): List<Product>
}
