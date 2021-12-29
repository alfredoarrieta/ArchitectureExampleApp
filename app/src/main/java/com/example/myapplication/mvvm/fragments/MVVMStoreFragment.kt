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
import com.example.myapplication.adapters.StoreProductAdapter
import com.example.myapplication.adapters.StoreProductInterface
import com.example.myapplication.animations.AnimationsProvider
import com.example.myapplication.data.model.Product
import com.example.myapplication.databinding.FragmentStoreBinding
import com.example.myapplication.mvvm.repositories.StoreRepository
import com.example.myapplication.mvvm.viemodels.MVVMViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MVVMStoreFragment : Fragment(), KoinComponent {

    private lateinit var binding: FragmentStoreBinding
    private val animationsProvider: AnimationsProvider by inject()
    private val animationHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStoreBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MVVMViewModel by activityViewModels()

        viewModel.onDataChanged.observe(this, { data ->
            binding.body.text = HtmlCompat.fromHtml(String.format("<b>Cart total is:</b> $%.2f",data.cartTotal), HtmlCompat.FROM_HTML_MODE_LEGACY)
            (binding.productList.adapter as StoreProductAdapter).updateProducts(data.products, data.cart)
        })

        binding.slidingView.post { binding.slidingView.translationX = binding.slidingView.width.toFloat() }

        binding.productList.layoutManager = GridLayoutManager(context, 2)
        binding.productList.adapter = StoreProductAdapter(arrayListOf(), object : StoreProductInterface {
            override fun onProductAdded(product: Product) {
                viewModel.addProductToCart(product)
                startSlidingViewAnimation()
            }
        })

        viewModel.getProducts()
    }

    private fun startSlidingViewAnimation() {
        animationsProvider.startLeftSlidingViewAnimation(binding.slidingView)
        animationHandler.removeCallbacksAndMessages(null)
        animationHandler.postDelayed({ binding.slidingView.let{ animationsProvider.finishLeftSlidingViewAnimation(binding.slidingView) } },3000)
    }
}