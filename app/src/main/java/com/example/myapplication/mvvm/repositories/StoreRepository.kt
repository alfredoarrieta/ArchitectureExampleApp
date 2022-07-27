package com.example.myapplication.mvvm.repositories

import com.example.myapplication.data.datasources.LocalDataSource
import com.example.myapplication.data.model.Product
import com.example.myapplication.data.model.StoreData

class StoreRepository(private val localDataSource: LocalDataSource) {

    private var _products : List<Product> = arrayListOf()
    private var _cart : List<Product> = arrayListOf()
    private var _cartTotal : Double = 0.0

    private val storeData get() = StoreData(_products, _cart, _cartTotal)

    suspend fun addProductToCart(product: Product): StoreData {
        val newList: MutableList<Product> = mutableListOf()
        newList.addAll(_cart)
        newList.firstOrNull{it.id == product.id}?.increaseAmount() ?: newList.add(if(product.amount == 0) product.increaseAmount() else product)
        updateCartProducts(newList)
        updateTotalPrice()
        return storeData
    }

    suspend fun removeProductFromCart(product: Product): StoreData {
        val newList: MutableList<Product> = mutableListOf()
        newList.addAll(_cart)
        newList.firstOrNull{it.id == product.id}?.decreaseAmount()?.let {
            if (it.amount == 0){ newList.remove(it) }
        }
        updateCartProducts(newList)
        updateTotalPrice()
        return storeData
    }

    private fun updateCartProducts(newList: MutableList<Product>) {
        _cart = newList
        localDataSource.saveCartProducts(newList)
    }

    private fun updateTotalPrice() {
        var cost = 0.0
        _cart.forEach { cost += it.price * it.amount }
        _cartTotal = cost
    }

    suspend fun getProducts(): StoreData {
        _products = localDataSource.getStoreProducts()
        _cart = localDataSource.getCartProducts()
        updateTotalPrice()
        return storeData
    }
}