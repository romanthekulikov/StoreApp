package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.domain.ProductEntity

@Dao
interface BasketDao {
    @Query("SELECT * FROM basket")
    fun getOrder(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProduct(product: ProductEntity)

    @Update
    fun updateCount(product: ProductEntity)

    @Query("DELETE FROM basket WHERE title = :productTitle")
    fun deleteProduct(productTitle: String)

    @Query("DELETE FROM basket")
    fun deleteBasket()
}