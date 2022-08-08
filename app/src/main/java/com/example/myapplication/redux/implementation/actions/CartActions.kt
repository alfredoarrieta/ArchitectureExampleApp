package com.example.myapplication.redux.implementation.actions

import com.example.myapplication.data.model.Product

open class CartActions : ReduxAction() {
    class SetCartProducts(val products: List<Product>) : CartActions()
    class AddProductToCart(val product: Product) : CartActions()
    class RemoveProductFromCart(val product: Product) : CartActions()
    class RestoreProductsFromCache : CartActions()
}