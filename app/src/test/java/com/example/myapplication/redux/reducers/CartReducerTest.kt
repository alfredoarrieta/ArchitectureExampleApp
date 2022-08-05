package com.example.myapplication.redux.reducers

import com.example.myapplication.data.model.Product
import com.example.myapplication.redux.implementation.AppState
import com.example.myapplication.redux.implementation.CartState
import com.example.myapplication.redux.implementation.actions.CartActions
import com.example.myapplication.redux.implementation.reducers.CartReducer
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CartReducerTest {

    private lateinit var cartReducer: CartReducer
    private lateinit var product: Product
    private lateinit var product2: Product

    @Before
    fun setup() {
        cartReducer = CartReducer()
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
    fun setProductsEmptyCartTest() {
        // GIVEN
        val products = listOf(product)
        val cartState = CartState()
        val appState = AppState(cartState = cartState)
        // WHEN
        val resultAppState = cartReducer.reduce(
            CartActions.SetCartProducts(products),
            appState
        )
        // THEN
        with(resultAppState.cartState) {
            assertEquals(1, this.products.size)
            assertEquals(products, this.products)
            assertEquals(6.95, cartTotal, 0.0)
        }
    }

    @Test
    fun setProductsTest() {
        // GIVEN
        val products = listOf(product)
        val cartState = CartState(products = listOf(product2), cartTotal = 3.05)
        val appState = AppState(cartState = cartState)
        // WHEN
        val resultAppState = cartReducer.reduce(
            CartActions.SetCartProducts(products),
            appState
        )
        // THEN
        with(resultAppState.cartState) {
            assertEquals(1, this.products.size)
            assertEquals(products, this.products)
            assertEquals(6.95, cartTotal, 0.0)
        }
    }

    @Test
    fun addProductTest() {
        // GIVEN
        val cartState = CartState()
        val appState = AppState(cartState = cartState)
        // WHEN
        val resultAppState = cartReducer.reduce(
            CartActions.AddProductToCart(product),
            appState
        )
        // THEN
        with(resultAppState.cartState) {
            assertEquals(1, this.products.size)
            assertEquals(product, this.products[0])
            assertEquals(6.95, cartTotal, 0.0)
        }
    }

    @Test
    fun increaseProductTest() {
        // GIVEN
        val products = listOf(product)
        val cartState = CartState(listOf(product))
        val appState = AppState(cartState = cartState)
        // WHEN
        val resultAppState = cartReducer.reduce(
            CartActions.AddProductToCart(product),
            appState
        )
        // THEN
        with(resultAppState.cartState) {
            assertEquals(1, this.products.size)
            assertEquals(product, this.products[0])
            assertEquals(2, this.products[0].amount)
            assertEquals(13.9, cartTotal, 0.0)
        }
    }

    @Test
    fun increaseDifferentProductTest() {
        // GIVEN
        val cartState = CartState(listOf(product))
        val appState = AppState(cartState = cartState)
        // WHEN
        val resultAppState = cartReducer.reduce(
            CartActions.AddProductToCart(product2),
            appState
        )
        // THEN
        with(resultAppState.cartState) {
            assertEquals(2, this.products.size)
            assertEquals(product, this.products[0])
            assertEquals(product2, this.products[1])
            assertEquals(1, this.products[0].amount)
            assertEquals(10.0, cartTotal, 0.0)
        }
    }

    @Test
    fun removeProductTest() {
        // GIVEN
        val products = listOf(product)
        val cartState = CartState(products)
        val appState = AppState(cartState = cartState)
        // WHEN
        val resultAppState = cartReducer.reduce(
            CartActions.RemoveProductFromCart(product),
            appState
        )
        // THEN
        with(resultAppState.cartState) {
            assertEquals(0, this.products.size)
            assertEquals(0.0, cartTotal, 0.0)
        }
    }

    @Test
    fun decreaseProductTest() {
        // GIVEN
        val products = listOf(product.copy(amount = 2))
        val cartState = CartState(products)
        val appState = AppState(cartState = cartState)
        // WHEN
        val resultAppState = cartReducer.reduce(
            CartActions.RemoveProductFromCart(product),
            appState
        )
        // THEN
        with(resultAppState.cartState) {
            assertEquals(1, this.products.size)
            assertEquals(6.95, cartTotal, 0.0)
        }
    }

    @Test
    fun removeNonExistingProductTest() {
        // GIVEN
        val products = listOf(product)
        val cartState = CartState(products)
        val appState = AppState(cartState = cartState)
        // WHEN
        val resultAppState = cartReducer.reduce(
            CartActions.RemoveProductFromCart(product2),
            appState
        )
        // THEN
        with(resultAppState.cartState) {
            assertEquals(1, this.products.size)
            assertEquals(6.95, cartTotal, 0.0)
        }
    }
}