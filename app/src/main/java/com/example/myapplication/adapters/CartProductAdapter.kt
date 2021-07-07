package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Product
import com.example.myapplication.databinding.ItemCartProductBinding
import com.squareup.picasso.Picasso

class CartProductAdapter(private val callback: CartProductInterface) : RecyclerView.Adapter<CartProductViewHolder>() {

    private lateinit var products: List<Product>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder(ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        holder.bind(products[position], callback)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setProducts(products: List<Product>){
        this.products = products
        notifyDataSetChanged()
    }
}

class CartProductViewHolder(private val binding: ItemCartProductBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product, callback: CartProductInterface){
        binding.amount.text = "${product.amount}x"
        Picasso.get().load(product.image).resize(100,120).placeholder(R.drawable.ic_android).into(binding.image)
        binding.name.text = product.name
        binding.price.text = "$${product.price}"
        binding.deleteButton.text = if(product.amount == 1) "Delete" else "Decrease"
        binding.deleteButton.setOnClickListener { callback.onProductDeleted(product) }
    }
}

interface CartProductInterface{
    fun onProductDeleted(product: Product)
}