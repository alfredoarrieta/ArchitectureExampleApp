package com.example.myapplication.redux.epics

import com.example.myapplication.data.datasources.LocalDataSource
import com.example.myapplication.data.model.Product
import com.example.myapplication.redux.implementation.AppStore
import com.example.myapplication.redux.implementation.actions.StoreActions
import com.example.myapplication.redux.implementation.epics.StoreEpic
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever

class StoreEpicTest {

    private lateinit var storeEpic: StoreEpic
    private lateinit var mockedLocalDataSource: LocalDataSource
    private lateinit var mockedStore: AppStore
    private lateinit var product: Product

    @Before
    fun setup() {
        mockedLocalDataSource = Mockito.mock(LocalDataSource::class.java)
        storeEpic = StoreEpic(mockedLocalDataSource)
        mockedStore = Mockito.mock(AppStore::class.java)
        product = Product(
            "id-0",
            "meat",
            6.95,
            "https://embed.widencdn.net/img/beef/melpznnl7q/800x600px/Top%20Sirloin%20Steak.psd?keep=c&u=7fueml"
        )
    }

    @Test
    fun getStoreProductsTest() {
        // GIVEN
        val products = listOf(product)
        var returnedProducts = emptyList<Product>()
        val onStoreProductsFetched = object : StoreActions.StoreProductsInterface {
            override fun onProductsFetched(products: List<Product>) {
                returnedProducts = products
            }
        }
        whenever(mockedLocalDataSource.getStoreProducts()).thenReturn(products)
        // WHEN
        storeEpic.actionReceived(
            StoreActions.GetStoreProductsEpic(onStoreProductsFetched),
            mockedStore
        )
        // THEN
        Mockito.verify(mockedLocalDataSource, times(1)).getStoreProducts()
        assertEquals(products, returnedProducts)
    }
}