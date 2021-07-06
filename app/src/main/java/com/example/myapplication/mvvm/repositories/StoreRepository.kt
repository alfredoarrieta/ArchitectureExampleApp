package com.example.myapplication.mvvm.repositories

import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.datasources.LocalDataSource
import com.example.myapplication.data.model.Product

class StoreRepository(private val localDataSource: LocalDataSource) {

    val cart : MutableLiveData<List<Product>> = MutableLiveData(localDataSource.getCartProducts())
    val cartTotal : MutableLiveData<Double> = MutableLiveData(0.0)

    init {
        updateTotalPrice()
    }

    fun addProductToCart(product: Product){
        val newList: MutableList<Product> = mutableListOf()
        cart.value?.let{ cartProducts -> newList.addAll(cartProducts) }
        newList.firstOrNull{it.id == product.id}?.increaseAmount() ?: newList.add(product.increaseAmount())
        updateCartProducts(newList)
        updateTotalPrice()
    }

    fun removeProductFromCart(product: Product){
        val newList: MutableList<Product> = mutableListOf()
        cart.value?.let{ cartProducts -> newList.addAll(cartProducts) }
        newList.firstOrNull{it.id == product.id}?.decreaseAmount()?.let {
            if (it.amount == 0){ newList.remove(it) }
        }
        updateCartProducts(newList)
        updateTotalPrice()
    }

    private fun updateCartProducts(newList: MutableList<Product>) {
        cart.value = newList
        localDataSource.saveCartProducts(newList)
    }

    private fun updateTotalPrice() {
        var cost = 0.0
        cart.value?.forEach { cost += it.price * it.amount }
        cartTotal.value = cost
    }

    fun getProducts(callback: ProductsCallback) {
        callback.onProductsFetched(localDataSource.getStoreProducts())
    }

    interface ProductsCallback{
        fun onProductsFetched(products: List<Product>)
    }
}