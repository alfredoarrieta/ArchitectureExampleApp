package com.example.myapplication.data.model

data class Product(val id: String, val name: String, val price: Double, val image: String, var amount: Int = 0){
    fun increaseAmount(): Product{
        amount++
        return this
    }

    fun decreaseAmount(): Product{
        amount--
        return this
    }
}