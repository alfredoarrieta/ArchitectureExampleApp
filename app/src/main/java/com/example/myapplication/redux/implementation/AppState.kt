package com.example.myapplication.redux.implementation

import com.example.myapplication.data.model.Product

data class AppState(
    val navigationState: NavigationState = NavigationState(),
    val cartState: CartState = CartState()
)

data class NavigationState(val lastFragment: FragmentType = FragmentType.STORE)

enum class FragmentType { STORE, CART, ABOUT, BOTH }

data class CartState(val products: List<Product> = mutableListOf(), val cartTotal: Double = 0.0)