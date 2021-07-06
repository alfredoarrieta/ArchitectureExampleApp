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
import com.example.myapplication.mvvm.viemodels.MVVMViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.item_cart_product.view.*

class MVVMCartFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MVVMViewModel by activityViewModels()

        viewModel.cartTotal.observe(this, { data ->
            checkoutButton.text = String.format("Cart Total $%.2f",data)
        })

        productList.layoutManager = LinearLayoutManager(context)
        productList.adapter = CartProductAdapter(object : CartProductInterface {
            override fun onProductDeleted(product: Product) {
                viewModel.removeProductFromCart(product)
            }
        })

        viewModel.cart.observe(this, { cartProducts ->
            (productList.adapter as CartProductAdapter).setProducts(cartProducts)
        })
    }
}

interface CartProductInterface{
    fun onProductDeleted(product: Product)
}

class CartProductViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    fun bind(product: Product, callback: CartProductInterface){
        view.amount.text = "${product.amount}x"
        Picasso.get().load(product.image).resize(100,120).placeholder(R.drawable.ic_android).into(view.image)
        view.name.text = product.name
        view.price.text = "$${product.price}"
        view.deleteButton.text = if(product.amount == 1) "Delete" else "Decrease"
        view.deleteButton.setOnClickListener { callback.onProductDeleted(product) }
    }
}

class CartProductAdapter(private val callback: CartProductInterface) : RecyclerView.Adapter<CartProductViewHolder>() {

    private lateinit var products: List<Product>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cart_product, parent, false))
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