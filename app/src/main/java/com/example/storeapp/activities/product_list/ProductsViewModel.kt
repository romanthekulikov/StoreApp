package com.example.storeapp.activities.product_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.Repository
import com.example.domain.entity.Product
import com.example.storeapp.StoreApp
import javax.inject.Inject

class ProductsViewModel : ViewModel() {
    @Inject
    lateinit var repository: Repository

    val productList: MutableLiveData<List<Product>> = MutableLiveData(listOf())
    val currentAmountText = MutableLiveData(0.0)
    val errorLoadDataFlag = MutableLiveData(false)
    lateinit var selectedProduct: Product

    init {
        StoreApp.appComponent.inject(this)
    }

    suspend fun getData() {
        if (productList.value!!.isEmpty()) {
            productList.postValue(repository.getProductsList())

            errorLoadDataFlag.postValue(false)
        }
    }

    suspend fun moveToBasket(product: Product) {
        currentAmountText.postValue(currentAmountText.value!! + product.price)
        repository.moveToBasket(product)
    }

    fun errorLoadDetect() {
        errorLoadDataFlag.postValue(true)
    }
}