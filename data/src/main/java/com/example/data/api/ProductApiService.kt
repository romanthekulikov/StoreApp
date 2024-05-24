package com.example.data.api

import com.example.data.api.models.ProductApi
import retrofit2.http.GET

interface ProductApiService {
    @GET("/products")
    suspend fun fetchProductList(): List<ProductApi>
}