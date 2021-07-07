package com.example.myapplication.redux.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.animations.AnimationsProvider
import com.example.myapplication.data.model.Product
import com.example.myapplication.databinding.FragmentCartBinding
import com.example.myapplication.databinding.ItemCartProductBinding
import com.example.myapplication.redux.implementation.actions.CartActions
import com.squareup.picasso.Picasso
import org.koin.core.component.inject

class ReduxCartFragment : ReduxFragment() {

    private lateinit var binding: FragmentCartBinding
    private val animationsProvider: AnimationsProvider by inject()
    private var entryAnimationPresented = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productList.layoutManager = LinearLayoutManager(context)
        binding.productList.adapter = ReduxCartProductAdapter(object : ReduxCartProductInterface {
            override fun onProductDeleted(product: Product) { appStore.dispatch(CartActions.RemoveProductFromCart(product)) }
        })

        subscriptions.add(appStore.getObservableState().subscribe{ state ->
            binding.checkoutButton.text = String.format("Cart Total $%.2f",state.cartState.cartTotal)
            (binding.productList.adapter as ReduxCartProductAdapter).setProducts(state.cartState.products)
        })
    }

    fun entryAnimation(){
        if(entryAnimationPresented.not()){
            binding.mainContainer.post { animationsProvider.entryBottomAnimation(binding.mainContainer) }
            entryAnimationPresented = true
        }
    }

    fun exitAnimation(callback: AnimationsProvider.AnimationEndCallback){
        if(entryAnimationPresented) {
            binding.mainContainer.post { animationsProvider.exitBottomAnimation(binding.mainContainer,callback) }
            entryAnimationPresented = false
        } else {
            callback.onAnimationEnd()
        }
    }
}

interface ReduxCartProductInterface{
    fun onProductDeleted(product: Product)
}

class ReduxCartProductViewHolder(private val binding: ItemCartProductBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product, callback: ReduxCartProductInterface){
        binding.amount.text = "${product.amount}x"
        Picasso.get().load(product.image).resize(100,120).placeholder(R.drawable.ic_android).into(binding.image)
        binding.name.text = product.name
        binding.price.text = "$${product.price}"
        binding.deleteButton.text = if(product.amount == 1) "Delete" else "Decrease"
        binding.deleteButton.setOnClickListener { callback.onProductDeleted(product) }
    }
}

class ReduxCartProductAdapter(private val callback: ReduxCartProductInterface) : RecyclerView.Adapter<ReduxCartProductViewHolder>() {

    private var products: List<Product> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReduxCartProductViewHolder {
        return ReduxCartProductViewHolder(ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ReduxCartProductViewHolder, position: Int) {
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