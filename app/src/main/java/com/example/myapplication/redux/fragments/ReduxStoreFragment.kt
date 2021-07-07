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
import com.example.myapplication.databinding.FragmentStoreBinding
import com.example.myapplication.databinding.ItemStoreProductBinding
import com.example.myapplication.redux.implementation.actions.CartActions
import com.example.myapplication.redux.implementation.actions.StoreActions
import com.squareup.picasso.Picasso
import org.koin.core.component.inject

class ReduxStoreFragment : ReduxFragment() {

    private lateinit var binding: FragmentStoreBinding
    private val animationsProvider: AnimationsProvider by inject()
    private val animationHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStoreBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appStore.dispatch(StoreActions.GetStoreProductsEpic(object : StoreActions.StoreProductsInterface {
            override fun onProductsFetched(products: List<Product>) {
                binding.productList.layoutManager = GridLayoutManager(context, 2)
                binding.productList.adapter = ReduxProductAdapter(products, object : ReduxStoreProductInterface {
                    override fun onProductAdded(product: Product) {
                        appStore.dispatch(CartActions.AddProductToCart(product))
                        startSlidingViewAnimation()
                    }
                })
            }
        }))

        subscriptions.add(appStore.getObservableState().subscribe{ state ->
            binding.body.text = HtmlCompat.fromHtml(String.format("<b>Cart total is:</b> $%.2f",state.cartState.cartTotal), HtmlCompat.FROM_HTML_MODE_LEGACY)
            (binding.productList.adapter as ReduxProductAdapter).updateProducts(state.cartState.products)
            binding.productList.adapter?.notifyDataSetChanged()
        })

        binding.slidingView.post { binding.slidingView.translationX = binding.slidingView.width.toFloat() }
    }

    private fun startSlidingViewAnimation() {
        animationsProvider.startLeftSlidingViewAnimation(binding.slidingView)
        animationHandler.removeCallbacksAndMessages(null)
        animationHandler.postDelayed({ animationsProvider.finishLeftSlidingViewAnimation(binding.slidingView) },3000)
    }
}

interface ReduxStoreProductInterface{
    fun onProductAdded(product: Product)
}

class ReduxProductViewHolder(private val binding: ItemStoreProductBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product, callback: ReduxStoreProductInterface){
        Picasso.get().load(product.image).resize(100,120).placeholder(R.drawable.ic_android).into(binding.image)
        binding.name.text = product.name
        binding.price.text = "$${product.price}"
        binding.addButton.text = if(product.amount == 0) "Add" else product.amount.toString()
        binding.addButton.setOnClickListener { callback.onProductAdded(product) }
    }
}

class ReduxProductAdapter(private var products: List<Product>, private val callback: ReduxStoreProductInterface) : RecyclerView.Adapter<ReduxProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReduxProductViewHolder {
        return ReduxProductViewHolder(ItemStoreProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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