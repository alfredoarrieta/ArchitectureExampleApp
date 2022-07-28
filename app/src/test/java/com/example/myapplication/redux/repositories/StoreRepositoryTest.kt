package com.example.myapplication.redux.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.data.datasources.LocalDataSource
import com.example.myapplication.data.model.Product
import com.example.myapplication.mvvm.repositories.StoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class StoreRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var storeRepository: StoreRepository
    private lateinit var mockedLocalDataSource: LocalDataSource

    private lateinit var product: Product
    private lateinit var product2: Product

    @Before
    fun setup() {
        mockedLocalDataSource = Mockito.mock(LocalDataSource::class.java)
        storeRepository = StoreRepository(mockedLocalDataSource)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getProductsTest() = runTest {
        Dispatchers.setMain(Dispatchers.IO)
        // GIVEN
        val storeProducts = listOf(product, product2)
        val cartProducts = listOf(product2)
        whenever(mockedLocalDataSource.getStoreProducts()).thenReturn(storeProducts)
        whenever(mockedLocalDataSource.getCartProducts()).thenReturn(cartProducts)
        // WHEN
        val resultStoreData = storeRepository.getProducts()
        // THEN
        Mockito.verify(mockedLocalDataSource, times(1)).getStoreProducts()
        Mockito.verify(mockedLocalDataSource, times(1)).getCartProducts()
        with(resultStoreData) {
            assertEquals(storeProducts, products)
            assertEquals(cartProducts, cart)
            assertEquals(3.05, cartTotal, 0.0)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addProductToCartTest() = runTest {
        Dispatchers.setMain(Dispatchers.IO)
        // GIVEN
        val storeProducts = listOf(product, product2)
        val cartProducts = listOf(product2)
        whenever(mockedLocalDataSource.getStoreProducts()).thenReturn(storeProducts)
        whenever(mockedLocalDataSource.getCartProducts()).thenReturn(cartProducts)
        storeRepository.getProducts()
        // WHEN
        val resultStoreData = storeRepository.addProductToCart(product)
        // THEN
        Mockito.verify(mockedLocalDataSource, times(1)).getStoreProducts()
        Mockito.verify(mockedLocalDataSource, times(1)).getCartProducts()
        Mockito.verify(mockedLocalDataSource, times(1)).saveCartProducts(any())
        with(resultStoreData) {
            assertEquals(storeProducts, products)
            assertEquals(2, cart.size)
            assertEquals(10.0, cartTotal, 0.0)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun increaseProductTest() = runTest {
        Dispatchers.setMain(Dispatchers.IO)
        // GIVEN
        val storeProducts = listOf(product, product2)
        val cartProducts = listOf(product2)
        whenever(mockedLocalDataSource.getStoreProducts()).thenReturn(storeProducts)
        whenever(mockedLocalDataSource.getCartProducts()).thenReturn(cartProducts)
        storeRepository.getProducts()
        // WHEN
        val resultStoreData = storeRepository.addProductToCart(product2)
        // THEN
        Mockito.verify(mockedLocalDataSource, times(1)).getStoreProducts()
        Mockito.verify(mockedLocalDataSource, times(1)).getCartProducts()
        Mockito.verify(mockedLocalDataSource, times(1)).saveCartProducts(any())
        with(resultStoreData) {
            assertEquals(storeProducts, products)
            assertEquals(1, cart.size)
            assertEquals(6.1, cartTotal, 0.0)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun removeProductTest() = runTest {
        Dispatchers.setMain(Dispatchers.IO)
        // GIVEN
        val storeProducts = listOf(product, product2)
        val cartProducts = listOf(product2)
        whenever(mockedLocalDataSource.getStoreProducts()).thenReturn(storeProducts)
        whenever(mockedLocalDataSource.getCartProducts()).thenReturn(cartProducts)
        storeRepository.getProducts()
        // WHEN
        val resultStoreData = storeRepository.removeProductFromCart(product2)
        // THEN
        Mockito.verify(mockedLocalDataSource, times(1)).getStoreProducts()
        Mockito.verify(mockedLocalDataSource, times(1)).getCartProducts()
        Mockito.verify(mockedLocalDataSource, times(1)).saveCartProducts(any())
        with(resultStoreData) {
            assertEquals(storeProducts, products)
            assertEquals(0, cart.size)
            assertEquals(0.0, cartTotal, 0.0)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun decreaseProductTest() = runTest {
        Dispatchers.setMain(Dispatchers.IO)
        // GIVEN
        val storeProducts = listOf(product, product2)
        val cartProducts = listOf(product2.copy(amount = 2))
        whenever(mockedLocalDataSource.getStoreProducts()).thenReturn(storeProducts)
        whenever(mockedLocalDataSource.getCartProducts()).thenReturn(cartProducts)
        storeRepository.getProducts()
        // WHEN
        val resultStoreData = storeRepository.removeProductFromCart(product2)
        // THEN
        Mockito.verify(mockedLocalDataSource, times(1)).getStoreProducts()
        Mockito.verify(mockedLocalDataSource, times(1)).getCartProducts()
        Mockito.verify(mockedLocalDataSource, times(1)).saveCartProducts(any())
        with(resultStoreData) {
            assertEquals(storeProducts, products)
            assertEquals(1, cart.size)
            assertEquals(3.05, cartTotal, 0.0)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun removeNonExistingProductTest() = runTest {
        Dispatchers.setMain(Dispatchers.IO)
        // GIVEN
        val storeProducts = listOf(product, product2)
        val cartProducts = listOf(product2)
        whenever(mockedLocalDataSource.getStoreProducts()).thenReturn(storeProducts)
        whenever(mockedLocalDataSource.getCartProducts()).thenReturn(cartProducts)
        storeRepository.getProducts()
        // WHEN
        val resultStoreData = storeRepository.removeProductFromCart(product)
        // THEN
        Mockito.verify(mockedLocalDataSource, times(1)).getStoreProducts()
        Mockito.verify(mockedLocalDataSource, times(1)).getCartProducts()
        Mockito.verify(mockedLocalDataSource, times(1)).saveCartProducts(any())
        with(resultStoreData) {
            assertEquals(storeProducts, products)
            assertEquals(1, cart.size)
            assertEquals(3.05, cartTotal, 0.0)
        }
    }

    @After
    fun finish() {
        Dispatchers.resetMain()
    }

}