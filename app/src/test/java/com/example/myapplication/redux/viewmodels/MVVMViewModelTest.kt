package com.example.myapplication.redux.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.data.datasources.LocalDataSource
import com.example.myapplication.data.model.Product
import com.example.myapplication.data.model.StoreData
import com.example.myapplication.mvvm.repositories.StoreRepository
import com.example.myapplication.mvvm.viemodels.MVVMViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.Mockito
import org.mockito.kotlin.whenever

class MVVMViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var mvvmViewModel: MVVMViewModel
    lateinit var mockedStoreRepository: StoreRepository
    lateinit var product: Product
    lateinit var storeData: StoreData

    @Before
    fun setup() {

        mvvmViewModel = MVVMViewModel()

        mockedStoreRepository = Mockito.mock(StoreRepository::class.java)

        val testModule = module {
            single { mockedStoreRepository }
        }

        startKoin {
            modules(testModule)
        }

        product = Product(
            "id-0",
            "meat",
            6.95,
            "https://embed.widencdn.net/img/beef/melpznnl7q/800x600px/Top%20Sirloin%20Steak.psd?keep=c&u=7fueml",
            amount = 1
        )

        storeData = StoreData(LocalDataSource.getStoreProducts(), emptyList(), 0.0)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getProductsTest() = runTest {
        Dispatchers.setMain(Dispatchers.IO)
        // GIVEN
        whenever(mockedStoreRepository.getProducts()).thenReturn(storeData.copy(cartTotal = 1.0))
        // WHEN
        mvvmViewModel.getProducts()
        // THEN
        mvvmViewModel.onDataChanged.observeForever {
            assertEquals(10, it.products.size)
            assertEquals(1.0, it.cartTotal, 0.0)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addProductToCartTest() = runTest {
        Dispatchers.setMain(Dispatchers.IO)
        // GIVEN
        whenever(mockedStoreRepository.addProductToCart(product)).thenReturn(storeData.copy(cartTotal = 2.0))
        // WHEN
        mvvmViewModel.addProductToCart(product)
        // THEN
        mvvmViewModel.onDataChanged.observeForever {
            assertEquals(10, it.products.size)
            assertEquals(2.0, it.cartTotal, 0.0)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun removeProductFromCartTest() = runTest {
        Dispatchers.setMain(Dispatchers.IO)
        // GIVEN
        whenever(mockedStoreRepository.removeProductFromCart(product)).thenReturn(storeData.copy(cartTotal = 3.0))
        // WHEN
        mvvmViewModel.removeProductFromCart(product)
        // THEN
        mvvmViewModel.onDataChanged.observeForever {
            assertEquals(10, it.products.size)
            assertEquals(3.0, it.cartTotal, 0.0)
        }
    }

    @After
    fun finish() {
        Dispatchers.resetMain()
        stopKoin()
    }

}