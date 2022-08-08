package com.example.myapplication.redux.implementation.reducers

import com.example.myapplication.data.model.Product
import com.example.myapplication.redux.implementation.AppState
import com.example.myapplication.redux.implementation.actions.CartActions
import com.example.myapplication.redux.implementation.actions.ReduxAction

class CartReducer : BaseReducer() {

    override fun reduce(action: ReduxAction, state: AppState): AppState {
        return when (action) {
            is CartActions.SetCartProducts -> setCartProducts(action.products, state)
            is CartActions.AddProductToCart -> addProductToCart(action.product, state)
            is CartActions.RemoveProductFromCart -> removeProductFromCart(action.product, state)
            else -> state
        }
    }

    private fun setCartProducts(products: List<Product>, state: AppState): AppState {
        return state.copy(
            cartState = state.cartState.copy(
                products = products,
                cartTotal = updateTotalPrice(products)
            )
        )
    }

    private fun addProductToCart(product: Product, state: AppState): AppState {
        val newList: MutableList<Product> = mutableListOf()
        state.cartState.products.let { cartProducts -> newList.addAll(cartProducts) }
        newList.firstOrNull { it.id == product.id }?.increaseAmount()
            ?: newList.add(if (product.amount == 0) product.increaseAmount() else product)
        return state.copy(
            cartState = state.cartState.copy(
                products = newList,
                cartTotal = updateTotalPrice(newList)
            )
        )
    }

    private fun removeProductFromCart(product: Product, state: AppState): AppState {
        val newList: MutableList<Product> = mutableListOf()
        state.cartState.products.let { cartProducts -> newList.addAll(cartProducts) }
        newList.firstOrNull { it.id == product.id }?.decreaseAmount()?.let {
            if (it.amount == 0) {
                newList.remove(it)
            }
        }
        return state.copy(
            cartState = state.cartState.copy(
                products = newList,
                cartTotal = updateTotalPrice(newList)
            )
        )
    }

    private fun updateTotalPrice(newList: List<Product>): Double {
        var cost = 0.0
        newList.forEach { cost += it.price * it.amount }
        return cost
    }
}