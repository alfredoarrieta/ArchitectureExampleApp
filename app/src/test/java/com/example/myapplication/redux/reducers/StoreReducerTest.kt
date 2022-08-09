package com.example.myapplication.redux.reducers

import com.example.myapplication.data.model.Product
import com.example.myapplication.redux.implementation.AppState
import com.example.myapplication.redux.implementation.StoreState
import com.example.myapplication.redux.implementation.actions.StoreActions
import com.example.myapplication.redux.implementation.reducers.StoreReducer
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class StoreReducerTest {

    private lateinit var storeReducer: StoreReducer
    private lateinit var product: Product
    private lateinit var product2: Product

    @Before
    fun setup() {
        storeReducer = StoreReducer()
        product = Product(
            "id-0",
            "meat",
            6.95,
            "https://embed.widencdn.net/img/beef/melpznnl7q/800x600px/Top%20Sirloin%20Steak.psd?keep=c&u=7fueml",
            amount = 1
        )
        product2 = Product(
            "id-1",
            "meat",
            3.05,
            "https://embed.widencdn.net/img/beef/melpznnl7q/800x600px/Top%20Sirloin%20Steak.psd?keep=c&u=7fueml",
            amount = 1
        )
    }

    @Test
    fun setStoreProducts() {
        // GIVEN
        val products = listOf(product)
        val storeState = StoreState()
        val appState = AppState(storeState = storeState)
        // WHEN
        val resultAppState = storeReducer.reduce(
            StoreActions.SetStoreProducts(products),
            appState
        )
        // THEN
        with(resultAppState.storeState) {
            Assert.assertEquals(1, this.products.size)
            Assert.assertEquals(products, this.products)
        }
    }
}
