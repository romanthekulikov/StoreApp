package com.example.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.entity.Product

@Entity(tableName = "basket")
data class ProductEntity(
    @PrimaryKey
    @ColumnInfo(name = "title")
    override var title: String,
    @ColumnInfo(name = "price")
    override var price: Double,
    @ColumnInfo(name = "image")
    override var image: String,
    @ColumnInfo(name = "description")
    override var description: String,
    @ColumnInfo(name = "count")
    var count: Int
): Product {
    constructor(product: Product, count: Int): this(product.title, product.price, product.image, product.description, count) {
        this.title = product.title
        this.price = product.price
        this.image = product.image
        this.description = product.description
        this.count = count
    }
}
