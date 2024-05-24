package com.example.storeapp.injection

import com.example.storeapp.activities.product_card.ProductCardViewModel
import com.example.storeapp.injection.modules.BindModule
import com.example.storeapp.injection.modules.NetworkModule
import com.example.storeapp.activities.product_list.ProductsViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [BindModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(productsViewModel: ProductsViewModel)
    fun inject(productsViewModel: ProductCardViewModel)
}