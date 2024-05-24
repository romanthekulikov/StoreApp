package com.example.storeapp.activities.product_card

import androidx.lifecycle.ViewModel
import com.example.domain.Repository
import com.example.domain.entity.Product
import com.example.storeapp.StoreApp
import javax.inject.Inject

class ProductCardViewModel(val product: Product, var inBasket: Boolean) : ViewModel() {
    @Inject
    lateinit var repository: Repository

    init {
        StoreApp.appComponent.inject(this)
    }

    suspend fun moveToBasket() {
        repository.moveToBasket(product)
    }
}