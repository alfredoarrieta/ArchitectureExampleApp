package com.example.myapplication.redux.implementation.epics

import com.example.myapplication.data.datasources.LocalDataSource
import com.example.myapplication.redux.implementation.AppStore
import com.example.myapplication.redux.implementation.actions.CartActions
import com.example.myapplication.redux.implementation.actions.ReduxAction

class CartEpic(private val localDataSource: LocalDataSource) : BaseEpic() {

    override fun actionReceived(action: ReduxAction, store: AppStore) {
        when (action) {
            is CartActions.AddProductToCart -> saveCartProducts(store)
            is CartActions.RemoveProductFromCart -> saveCartProducts(store)
            is CartActions.RestoreProductsFromCache -> getCartProducts(store)
        }
    }

    private fun getCartProducts(store: AppStore) {
        store.dispatch(CartActions.SetCartProducts(localDataSource.getCartProducts()))
    }

    private fun saveCartProducts(store: AppStore) {
        localDataSource.saveCartProducts(store.getState().cartState.products)
    }

}