package com.example.domain

import com.example.domain.entity.Product

interface Repository {
    suspend fun getProductsList(): List<Product>
    suspend fun moveToBasket(product: Product)
    suspend fun deleteFromBasket(product: Product)
}