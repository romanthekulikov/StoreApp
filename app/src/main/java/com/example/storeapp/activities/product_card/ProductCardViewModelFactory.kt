package com.example.storeapp.activities.product_card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.entity.Product

@Suppress("UNCHECKED_CAST")
class ProductCardViewModelFactory(private val product: Product, private val inBasket: Boolean) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductCardViewModel(product, inBasket) as T
    }
}