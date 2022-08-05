package com.example.myapplication.redux.epics

import com.example.myapplication.data.datasources.LocalDataSource
import com.example.myapplication.data.model.Product
import com.example.myapplication.redux.implementation.AppState
import com.example.myapplication.redux.implementation.AppStore
import com.example.myapplication.redux.implementation.CartState
import com.example.myapplication.redux.implementation.actions.CartActions
import com.example.myapplication.redux.implementation.epics.CartEpic
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever

class CartEpicTest {

    private lateinit var cartEpic: CartEpic
    private lateinit var mockedLocalDataSource: LocalDataSource
    private lateinit var mockedStore: AppStore
    private lateinit var mockedAppState: AppState
    private lateinit var cartState: CartState
    private lateinit var product: Product

    @Before
    fun setup() {
        mockedLocalDataSource = Mockito.mock(LocalDataSource::class.java)
        mockedStore = Mockito.mock(AppStore::class.java)
        mockedAppState = Mockito.mock(AppState::class.java)
        cartEpic = CartEpic(mockedLocalDataSource)
        product = Product(
            "id-0",
            "meat",
            6.95,
            "https://embed.widencdn.net/img/beef/melpznnl7q/800x600px/Top%20Sirloin%20Steak.psd?keep=c&u=7fueml"
        )
    }

    @Test
    fun addProductTest() {
        // GIVEN
        val products = listOf(product)
        cartState = CartState(products)
        whenever(mockedStore.getState()).thenReturn(mockedAppState)
        whenever(mockedAppState.cartState).thenReturn(cartState)
        // WHEN
        cartEpic.actionReceived(
            CartActions.AddProductToCart(product),
            mockedStore
        )
        // THEN
        Mockito.verify(mockedStore, times(1)).getState()
        Mockito.verify(mockedAppState, times(1)).cartState
        Mockito.verify(mockedLocalDataSource, times(1)).saveCartProducts(products)
    }

    @Test
    fun removeProductTest() {
        // GIVEN
        val products = listOf(product)
        cartState = CartState(products)
        whenever(mockedStore.getState()).thenReturn(mockedAppState)
        whenever(mockedAppState.cartState).thenReturn(cartState)
        // WHEN
        cartEpic.actionReceived(
            CartActions.RemoveProductFromCart(product),
            mockedStore
        )
        // THEN
        Mockito.verify(mockedStore, times(1)).getState()
        Mockito.verify(mockedAppState, times(1)).cartState
        Mockito.verify(mockedLocalDataSource, times(1)).saveCartProducts(products)
    }

    @Test
    fun restoreProductsTest() {
        // GIVEN
        val products = listOf(product)
        whenever(mockedLocalDataSource.getCartProducts()).thenReturn(products)
        // WHEN
        cartEpic.actionReceived(
            CartActions.RestoreProductsFromCache(),
            mockedStore
        )
        // THEN
        Mockito.verify(mockedLocalDataSource, times(1)).getCartProducts()
        Mockito.verify(mockedStore, times(1)).dispatch(any<CartActions.SetCartProducts>())
    }
}