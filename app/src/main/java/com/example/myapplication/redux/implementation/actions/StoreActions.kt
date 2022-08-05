package com.example.myapplication.redux.implementation.actions

import com.example.myapplication.data.model.Product

open class StoreActions : ReduxAction() {
    class GetStoreProducts : StoreActions()
    class SetStoreProducts(val products: List<Product>) : StoreActions()
}