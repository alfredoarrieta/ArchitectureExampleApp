package com.example.myapplication.mvvm.viemodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.Product
import com.example.myapplication.mvvm.repositories.StoreRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MVVMViewModel: ViewModel(), KoinComponent {

    private val storeRepository: StoreRepository by inject()

    var lastFragment = FragmentType.STORE
    val cart: LiveData<List<Product>> = storeRepository.cart
    val cartTotal: LiveData<Double> = storeRepository.cartTotal

    enum class FragmentType{ STORE, CART, ABOUT, BOTH }

    fun getProducts(callback: StoreRepository.ProductsCallback) {
        storeRepository.getProducts(callback)
    }

    fun addProductToCart(product: Product){
        storeRepository.addProductToCart(product)
    }

    fun removeProductFromCart(product: Product){
        storeRepository.removeProductFromCart(product)
    }
}