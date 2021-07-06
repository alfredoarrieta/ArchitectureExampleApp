package com.example.myapplication.redux.implementation.actions

import com.example.myapplication.data.model.Product

open class StoreActions: ReduxAction() {
    class GetStoreProductsEpic(val callback: StoreProductsInterface): StoreActions()

    interface StoreProductsInterface{
        fun onProductsFetched(products: List<Product>)
    }
}