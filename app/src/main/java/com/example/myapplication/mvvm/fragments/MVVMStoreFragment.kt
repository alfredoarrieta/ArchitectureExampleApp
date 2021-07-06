package com.example.myapplication.mvvm.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.animations.AnimationsProvider
import com.example.myapplication.data.model.Product
import com.example.myapplication.mvvm.repositories.StoreRepository
import com.example.myapplication.mvvm.viemodels.MVVMViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_store.*
import kotlinx.android.synthetic.main.item_cart_product.view.image
import kotlinx.android.synthetic.main.item_cart_product.view.name
import kotlinx.android.synthetic.main.item_cart_product.view.price
import kotlinx.android.synthetic.main.item_store_product.view.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MVVMStoreFragment : Fragment(), KoinComponent {

    private val animationsProvider: AnimationsProvider by inject()
    private val animationHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_store, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MVVMViewModel by activityViewModels()

        viewModel.cartTotal.observe(this, { data ->
            body.text = HtmlCompat.fromHtml(String.format("<b>Cart total is:</b> $%.2f",data), HtmlCompat.FROM_HTML_MODE_LEGACY)
        })

        viewModel.getProducts(object : StoreRepository.ProductsCallback {
            override fun onProductsFetched(products: List<Product>) {
                productList.layoutManager = GridLayoutManager(context, 2)
                productList.adapter = ProductAdapter(products, object : StoreProductInterface {
                    override fun onProductAdded(product: Product) {
                        viewModel.addProductToCart(product)
                        startSlidingViewAnimation()
                    }
                })
            }
        })

        viewModel.cart.observe(this, { products ->
            (productList.adapter as ProductAdapter).updateProducts(products)
            productList.adapter?.notifyDataSetChanged()
        })

        slidingView.post { slidingView.translationX = slidingView.width.toFloat() }
    }

    private fun startSlidingViewAnimation() {
        animationsProvider.startLeftSlidingViewAnimation(slidingView)
        animationHandler.removeCallbacksAndMessages(null)
        animationHandler.postDelayed({ slidingView?.let{ animationsProvider.finishLeftSlidingViewAnimation(slidingView) } },3000)
    }
}

interface StoreProductInterface{
    fun onProductAdded(product: Product)
}

class ProductViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    fun bind(product: Product, callback: StoreProductInterface){
        Picasso.get().load(product.image).resize(100,120).placeholder(R.drawable.ic_android).into(view.image)
        view.name.text = product.name
        view.price.text = "$${product.price}"
        view.addButton.text = if(product.amount == 0) "Add" else product.amount.toString()
        view.addButton.setOnClickListener { callback.onProductAdded(product) }
    }
}

class ProductAdapter(private var products: List<Product>, private val callback: StoreProductInterface) : RecyclerView.Adapter<ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_store_product, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
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