package com.example.myapplication.mvvm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Product
import com.example.myapplication.databinding.FragmentCartBinding
import com.example.myapplication.databinding.ItemCartProductBinding
import com.example.myapplication.mvvm.viemodels.MVVMViewModel
import com.squareup.picasso.Picasso

class MVVMCartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MVVMViewModel by activityViewModels()

        viewModel.cartTotal.observe(this, { data ->
            binding.checkoutButton.text = String.format("Cart Total $%.2f",data)
        })

        binding.productList.layoutManager = LinearLayoutManager(context)
        binding.productList.adapter = CartProductAdapter(object : CartProductInterface {
            override fun onProductDeleted(product: Product) {
                viewModel.removeProductFromCart(product)
            }
        })

        viewModel.cart.observe(this, { cartProducts ->
            (binding.productList.adapter as CartProductAdapter).setProducts(cartProducts)
        })
    }
}

interface CartProductInterface{
    fun onProductDeleted(product: Product)
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