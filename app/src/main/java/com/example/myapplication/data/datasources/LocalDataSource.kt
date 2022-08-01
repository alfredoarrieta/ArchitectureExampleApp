package com.example.myapplication.data.datasources

import android.content.SharedPreferences
import com.example.myapplication.data.model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalDataSource(private val gson: Gson, private val sharedPreferences: SharedPreferences) {

    private val cartProductsKey = "CART_PRODUCTS"

    companion object {
        fun getStoreProducts(): List<Product> {
            val products = mutableListOf<Product>()
            products.add(
                Product(
                    "id-0",
                    "meat",
                    6.95,
                    "https://embed.widencdn.net/img/beef/melpznnl7q/800x600px/Top%20Sirloin%20Steak.psd?keep=c&u=7fueml"
                )
            )
            products.add(
                Product(
                    "id-1",
                    "chips",
                    3.45,
                    "https://images-na.ssl-images-amazon.com/images/I/81vJyb43URL._SL1500_.jpg"
                )
            )
            products.add(
                Product(
                    "id-2",
                    "milk",
                    2.55,
                    "https://target.scene7.com/is/image/Target/GUEST_419f1169-a698-45a1-8d89-ad28136ba841?wid=488&hei=488&fmt=pjpeg"
                )
            )
            products.add(
                Product(
                    "id-3",
                    "coke",
                    5.95,
                    "https://twapp.ie/wp-content/uploads/2020/04/can-of-coke-330ml.jpg"
                )
            )
            products.add(
                Product(
                    "id-4",
                    "beer",
                    1.45,
                    "https://www.heineken.com/media-us/p0bazdia/heineken-00-bottle.png?quality=85"
                )
            )
            products.add(
                Product(
                    "id-5",
                    "wine",
                    10.55,
                    "https://images-na.ssl-images-amazon.com/images/I/51uRLiaqWLL._SL1342_.jpg"
                )
            )
            products.add(
                Product(
                    "id-6",
                    "salad",
                    7.95,
                    "https://media-cdn.tripadvisor.com/media/photo-p/11/71/26/39/salad-lab.jpg"
                )
            )
            products.add(
                Product(
                    "id-7",
                    "pork",
                    5.95,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxkJ8R_w_JNY1DhSwrku-vjtfD2_GnifpZew&usqp=CAU"
                )
            )
            products.add(
                Product(
                    "id-8",
                    "chicken",
                    5.45,
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQci9pUjHeHzKS9C-QJLbsjwWwSBIjJ4c53kw&usqp=CAU"
                )
            )
            products.add(
                Product(
                    "id-9",
                    "lettuce",
                    2.55,
                    "https://cdn.britannica.com/77/170677-050-F7333D51/lettuce.jpg"
                )
            )
            return products
        }
    }

    fun getStoreProducts(): List<Product> {
        return LocalDataSource.getStoreProducts()
    }

    fun saveCartProducts(products: List<Product>){
        if(products.isNotEmpty()){
            sharedPreferences.edit().apply {
                putString(cartProductsKey, gson.toJson(products))
                apply()
            }
        }
    }

    fun getCartProducts(): List<Product>{
        var products = listOf<Product>()
        sharedPreferences.getString(cartProductsKey, null)?.let{
            products = gson.fromJson(it, object: TypeToken<List<Product>>() {}.type)
                ?: emptyList()
        }
        return products
    }
}