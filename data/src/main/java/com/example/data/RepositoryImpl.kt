package com.example.data

import com.example.data.api.ProductApiService
import com.example.data.database.AppDatabase
import com.example.domain.ProductEntity
import com.example.domain.Repository
import com.example.domain.entity.Product
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val productService: ProductApiService) : Repository {
    private var db = AppDatabase.db
    override suspend fun getProductsListFromApi(): List<Product> {
        return productService.fetchProductList()
    }

    override suspend fun getProductsListFromDB(): List<ProductEntity> {
        return db.basketDao().getOrder()
    }

    override suspend fun updateProduct(product: ProductEntity) {
        db.basketDao().updateCount(product)
    }

    override suspend fun moveToBasket(product: Product) {
        db.basketDao().addProduct(ProductEntity(product, 1))
    }

    override suspend fun deleteFromBasket(productTitle: String) {
        db.basketDao().deleteProduct(productTitle)
    }

    override suspend fun deleteBasket() {
        db.basketDao().deleteBasket()
    }
}