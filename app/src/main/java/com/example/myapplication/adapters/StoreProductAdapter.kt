package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Product
import com.example.myapplication.databinding.ItemStoreProductBinding
import com.squareup.picasso.Picasso

class StoreProductAdapter(
    private var products: List<Product>,
    private val callback: StoreProductInterface
) :
    ListAdapter<Product, StoreProductViewHolder>(StoreProductDifferenceCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreProductViewHolder {
        return StoreProductViewHolder(
            ItemStoreProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StoreProductViewHolder, position: Int) {
        holder.bind(products[position], callback)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun updateProducts(products: List<Product>?, cart: List<Product>?) {
        products?.let { products ->
            if (this.products.isEmpty()) {
                this.products = products
            }
            cart?.forEach { cartProduct ->
                this.products.firstOrNull { it.id == cartProduct.id }?.let {
                    it.amount = cartProduct.amount
                }
            }
            notifyDataSetChanged()
        }
    }
}

object StoreProductDifferenceCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.amount == newItem.amount
    }
}

class StoreProductViewHolder(private val binding: ItemStoreProductBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product, callback: StoreProductInterface) {
        Picasso.get().load(product.image).resize(100, 120).placeholder(R.drawable.ic_android)
            .into(binding.image)
        binding.name.text = product.name
        binding.price.text = "$${product.price}"
        binding.addButton.text = if (product.amount == 0) "Add" else product.amount.toString()
        binding.addButton.setOnClickListener { callback.onProductAdded(product) }
    }
}

interface StoreProductInterface {
    fun onProductAdded(product: Product)
}