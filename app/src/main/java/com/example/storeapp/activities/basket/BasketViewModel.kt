package com.example.storeapp.activities.basket

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.ProductEntity
import com.example.domain.Repository
import com.example.storeapp.StoreApp
import javax.inject.Inject

class BasketViewModel : ViewModel() {
    @Inject
    lateinit var repository: Repository
    var list = mutableListOf<ProductEntity>()
    var currentAmount = MutableLiveData(0.0)

    init {
        StoreApp.appComponent.inject(this)
    }

    suspend fun getData() {
        list = repository.getProductsListFromDB().toMutableList()
        var newAmount = 0.0
        list.forEach {
            newAmount += it.price * it.count
        }

        currentAmount.postValue(newAmount)
    }

    suspend fun increaseCount(product: ProductEntity) {
        currentAmount.postValue(currentAmount.value!! + product.price)
        repository.updateProduct(product)
    }

    suspend fun decreaseCount(product: ProductEntity) {
        currentAmount.postValue(currentAmount.value!! - product.price)
        repository.updateProduct(product)
    }

    suspend fun deleteProduct(productEntity: ProductEntity) {
        list.removeIf { it.title == productEntity.title }
        currentAmount.postValue(currentAmount.value!! - productEntity.price * productEntity.count)
        repository.deleteFromBasket(productEntity.title)
    }

    suspend fun makeOrder() {
        repository.deleteBasket()
    }

    private fun sendOrder() {
        // in feature
    }
}