package com.example.myapplication.redux.implementation

import com.example.myapplication.data.model.Product

data class AppState(
    val navigationState: NavigationState = NavigationState(),
    val cartState: CartState = CartState(),
    val storeState: StoreState = StoreState()
)

data class NavigationState(val lastFragment: FragmentType = FragmentType.STORE)

enum class FragmentType { STORE, CART, ABOUT, BOTH }

data class StoreState(val products: List<Product> = mutableListOf())

data class CartState(val products: List<Product> = mutableListOf(), val cartTotal: Double = 0.0)