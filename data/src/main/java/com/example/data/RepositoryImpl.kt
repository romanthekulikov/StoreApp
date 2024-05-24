package com.example.data

import com.example.data.api.ProductApiService
import com.example.domain.Repository
import com.example.domain.entity.Product
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val productService: ProductApiService) : Repository {
    override suspend fun getProductsList(): List<Product> {
        return productService.fetchProductList()
    }

    override suspend fun moveToBasket(product: Product) {

    }

    override suspend fun deleteFromBasket(product: Product) {
        TODO("Not yet implemented")
    }
}