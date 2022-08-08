package com.example.myapplication.mvvm.viemodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Product
import com.example.myapplication.data.model.StoreData
import com.example.myapplication.mvvm.repositories.StoreRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MVVMViewModel : ViewModel(), KoinComponent {

    private val storeRepository: StoreRepository by inject()

    var lastFragment = FragmentType.STORE

    private val _onDataChanged: MutableLiveData<StoreData> = MutableLiveData()
    val onDataChanged: LiveData<StoreData> = _onDataChanged

    enum class FragmentType { STORE, CART, ABOUT, BOTH }

    fun getProducts() {
        viewModelScope.launch {
            _onDataChanged.postValue(storeRepository.getProducts())
        }
    }

    fun addProductToCart(product: Product) {
        viewModelScope.launch {
            _onDataChanged.postValue(storeRepository.addProductToCart(product))
        }
    }

    fun removeProductFromCart(product: Product) {
        viewModelScope.launch {
            _onDataChanged.postValue(storeRepository.removeProductFromCart(product))
        }
    }
}