package com.example.myapplication.redux.datasource

import android.content.SharedPreferences
import com.example.myapplication.data.datasources.LocalDataSource
import com.example.myapplication.data.model.Product
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.internal.verification.Times
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class LocalDataSourceTest {

    private lateinit var localDataSource: LocalDataSource
    private lateinit var gson: Gson
    private lateinit var mockedSharedPreferences: SharedPreferences
    private lateinit var mockedSharedPreferencesEditor: SharedPreferences.Editor
    lateinit var product: Product

    @Before
    fun setup() {

        gson = Gson()
        mockedSharedPreferences = Mockito.mock(SharedPreferences::class.java)
        mockedSharedPreferencesEditor = Mockito.mock(SharedPreferences.Editor::class.java)

        localDataSource = LocalDataSource(
            gson,
            mockedSharedPreferences
        )

        product = Product(
            "id-0",
            "meat",
            6.95,
            "https://embed.widencdn.net/img/beef/melpznnl7q/800x600px/Top%20Sirloin%20Steak.psd?keep=c&u=7fueml",
            amount = 1
        )
    }

    @Test
    fun getStoreProductsTest() {
        // GIVEN
        val products = LocalDataSource.getStoreProducts()
        // WHEN
        val result = localDataSource.getStoreProducts()
        // THEN
        assertEquals(products, result)
    }

    @Test
    fun saveCartProductsEmptyTest() {
        // GIVEN
        val products = emptyList<Product>()
        whenever(mockedSharedPreferences.edit()).thenReturn(mockedSharedPreferencesEditor)
        whenever(mockedSharedPreferencesEditor.putString(any(), any())).thenReturn(mockedSharedPreferencesEditor)
        // WHEN
        localDataSource.saveCartProducts(products)
        // THEN
        Mockito.verify(mockedSharedPreferences, Times(0)).edit()
        Mockito.verify(mockedSharedPreferencesEditor, Times(0)).putString(any(), any())
    }

    @Test
    fun saveCartProductsTest() {
        // GIVEN
        val products = listOf(product)
        whenever(mockedSharedPreferences.edit()).thenReturn(mockedSharedPreferencesEditor)
        // WHEN
        localDataSource.saveCartProducts(products)
        // THEN
        Mockito.verify(mockedSharedPreferences, Times(1)).edit()
        Mockito.verify(mockedSharedPreferencesEditor, Times(1)).putString(any(), any())
        Mockito.verify(mockedSharedPreferencesEditor, Times(1)).apply()
    }

    @Test
    fun getCartProductsNullTest() {
        // GIVEN
        whenever(mockedSharedPreferences.getString(any(), any())).thenReturn(null)
        // WHEN
        val result = localDataSource.getCartProducts()
        // THEN
        assertEquals(0, result.size)
    }

    @Test
    fun getCartProductsEmptyTest() {
        // GIVEN
        whenever(mockedSharedPreferences.getString(any(), any())).thenReturn("")
        // WHEN
        val result = localDataSource.getCartProducts()
        // THEN
        assertEquals(0, result.size)
    }

    @Test
    fun getCartProductsOtherContentTest() {
        // GIVEN
        whenever(mockedSharedPreferences.getString(any(), any())).thenReturn(" this is a test string ")
        // WHEN
        val result = localDataSource.getCartProducts()
        // THEN
        assertEquals(0, result.size)
    }
}