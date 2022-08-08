package com.example.myapplication.redux.implementation.reducers

import com.example.myapplication.data.model.Product
import com.example.myapplication.redux.implementation.AppState
import com.example.myapplication.redux.implementation.actions.ReduxAction
import com.example.myapplication.redux.implementation.actions.StoreActions

class StoreReducer : BaseReducer() {

    override fun reduce(action: ReduxAction, state: AppState): AppState {
        return when (action) {
            is StoreActions.SetStoreProducts -> setStoreProducts(action.products, state)
            else -> state
        }
    }

    private fun setStoreProducts(products: List<Product>, state: AppState): AppState {
        return state.copy(storeState = state.storeState.copy(products = products))
    }
}
