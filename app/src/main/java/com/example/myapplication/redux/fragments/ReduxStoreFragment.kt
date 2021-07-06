package com.example.myapplication.redux.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.animations.AnimationsProvider
import com.example.myapplication.data.model.Product
import com.example.myapplication.redux.implementation.actions.CartActions
import com.example.myapplication.redux.implementation.actions.StoreActions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_store.*
import kotlinx.android.synthetic.main.item_cart_product.view.image
import kotlinx.android.synthetic.main.item_cart_product.view.name
import kotlinx.android.synthetic.main.item_cart_product.view.price
import kotlinx.android.synthetic.main.item_store_product.view.*
import org.koin.core.component.inject

class ReduxStoreFragment : ReduxFragment() {

    private val animationsProvider: AnimationsProvider by inject()
    private val animationHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_store, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appStore.dispatch(StoreActions.GetStoreProductsEpic(object : StoreActions.StoreProductsInterface {
            override fun onProductsFetched(products: List<Product>) {
                productList.layoutManager = GridLayoutManager(context, 2)
                productList.adapter = ReduxProductAdapter(products, object : ReduxStoreProductInterface {
                    override fun onProductAdded(product: Product) {
                        appStore.dispatch(CartActions.AddProductToCart(product))
                        startSlidingViewAnimation()
                    }
                })
            }
        }))

        subscriptions.add(appStore.getObservableState().subscribe{ state ->
            body.text = HtmlCompat.fromHtml(String.format("<b>Cart total is:</b> $%.2f",state.cartState.cartTotal), HtmlCompat.FROM_HTML_MODE_LEGACY)
            (productList.adapter as ReduxProductAdapter).updateProducts(state.cartState.products)
            productList.adapter?.notifyDataSetChanged()
        })

        slidingView.post { slidingView.translationX = slidingView.width.toFloat() }
    }

    private fun startSlidingViewAnimation() {
        animationsProvider.startLeftSlidingViewAnimation(slidingView)
        animationHandler.removeCallbacksAndMessages(null)
        animationHandler.postDelayed({ animationsProvider.finishLeftSlidingViewAnimation(slidingView) },3000)
    }
}

interface ReduxStoreProductInterface{
    fun onProductAdded(product: Product)
}

class ReduxProductViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    fun bind(product: Product, callback: ReduxStoreProductInterface){
        Picasso.get().load(product.image).resize(100,120).placeholder(R.drawable.ic_android).into(view.image)
        view.name.text = product.name
        view.price.text = "$${product.price}"
        view.addButton.text = if(product.amount == 0) "Add" else product.amount.toString()
        view.addButton.setOnClickListener { callback.onProductAdded(product) }
    }
}

class ReduxProductAdapter(private var products: List<Product>, private val callback: ReduxStoreProductInterface) : RecyclerView.Adapter<ReduxProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReduxProductViewHolder {
        return ReduxProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_store_product, parent, false))
    }

    override fun onBindViewHolder(holder: ReduxProductViewHolder, position: Int) {
        holder.bind(products[position], callback)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun updateProducts(products: List<Product>?) {
        products?.forEach { cartProduct ->
            this.products.firstOrNull { it.id == cartProduct.id }?.let{
                it.amount = cartProduct.amount
            }
        }

    }
}