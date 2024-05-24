package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.database.entities.ProductEntity

@Dao
interface BasketDao {
    @Query("SELECT * FROM basket")
    fun getOrder(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProduct(product: ProductEntity)

    @Update
    fun updateCount(product: ProductEntity)

    @Delete
    fun deleteProduct(product: ProductEntity)
}