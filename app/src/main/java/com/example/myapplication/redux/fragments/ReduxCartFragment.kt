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
import com.example.myapplication.redux.implementation.actions.CartActions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.mainContainer
import kotlinx.android.synthetic.main.item_cart_product.view.*
import org.koin.core.component.inject

class ReduxCartFragment : ReduxFragment() {

    private val animationsProvider: AnimationsProvider by inject()
    private var entryAnimationPresented = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productList.layoutManager = LinearLayoutManager(context)
        productList.adapter = ReduxCartProductAdapter(object : ReduxCartProductInterface {
            override fun onProductDeleted(product: Product) { appStore.dispatch(CartActions.RemoveProductFromCart(product)) }
        })

        subscriptions.add(appStore.getObservableState().subscribe{ state ->
            checkoutButton.text = String.format("Cart Total $%.2f",state.cartState.cartTotal)
            (productList.adapter as ReduxCartProductAdapter).setProducts(state.cartState.products)
        })
    }

    fun entryAnimation(){
        if(entryAnimationPresented.not()){
            mainContainer?.post { animationsProvider.entryBottomAnimation(mainContainer) }
            entryAnimationPresented = true
        }
    }

    fun exitAnimation(callback: AnimationsProvider.AnimationEndCallback){
        if(entryAnimationPresented) {
            mainContainer?.post { animationsProvider.exitBottomAnimation(mainContainer,callback) }
            entryAnimationPresented = false
        } else {
            callback.onAnimationEnd()
        }
    }
}

interface ReduxCartProductInterface{
    fun onProductDeleted(product: Product)
}

class ReduxCartProductViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    fun bind(product: Product, callback: ReduxCartProductInterface){
        view.amount.text = "${product.amount}x"
        Picasso.get().load(product.image).resize(100,120).placeholder(R.drawable.ic_android).into(view.image)
        view.name.text = product.name
        view.price.text = "$${product.price}"
        view.deleteButton.text = if(product.amount == 1) "Delete" else "Decrease"
        view.deleteButton.setOnClickListener { callback.onProductDeleted(product) }
    }
}

class ReduxCartProductAdapter(private val callback: ReduxCartProductInterface) : RecyclerView.Adapter<ReduxCartProductViewHolder>() {

    private var products: List<Product> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReduxCartProductViewHolder {
        return ReduxCartProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cart_product, parent, false))
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