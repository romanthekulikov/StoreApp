package com.example.data.api.models

import com.example.domain.entity.Product
import com.squareup.moshi.Json

data class ProductApi(
    @Json(name = "title")
    override val title: String,
    @Json(name = "price")
    override val price: Double,
    @Json(name = "image")
    override val image: String,
    @Json(name = "description")
    override val description: String
) : Product