package com.example.domain

import com.example.domain.entity.Product

interface Repository {
    suspend fun getProductsListFromApi(): List<Product>
    suspend fun getProductsListFromDB(): List<ProductEntity>
    suspend fun updateProduct(product: ProductEntity)
    suspend fun moveToBasket(product: Product)
    suspend fun deleteFromBasket(productTitle: String)
    suspend fun deleteBasket()
}