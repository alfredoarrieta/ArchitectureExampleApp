package com.example.myapplication.redux.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapters.CartProductAdapter
import com.example.myapplication.adapters.CartProductInterface
import com.example.myapplication.animations.AnimationsProvider
import com.example.myapplication.data.model.Product
import com.example.myapplication.databinding.FragmentCartBinding
import com.example.myapplication.redux.implementation.actions.CartActions
import org.koin.core.component.inject

class ReduxCartFragment : ReduxFragment() {

    private lateinit var binding: FragmentCartBinding
    private val animationsProvider: AnimationsProvider by inject()
    private var entryAnimationPresented = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productList.layoutManager = LinearLayoutManager(context)
        binding.productList.adapter = CartProductAdapter(object : CartProductInterface {
            override fun onProductDeleted(product: Product) {
                appStore.dispatch(CartActions.RemoveProductFromCart(product))
            }
        })

        subscriptions.add(appStore.getObservableState().subscribe { state ->
            binding.checkoutButton.text =
                String.format("Cart Total $%.2f", state.cartState.cartTotal)
            (binding.productList.adapter as CartProductAdapter).setProducts(state.cartState.products)
        })
    }

    fun entryAnimation() {
        if (entryAnimationPresented.not()) {
            binding.mainContainer.post { animationsProvider.entryBottomAnimation(binding.mainContainer) }
            entryAnimationPresented = true
        }
    }

    fun exitAnimation(callback: AnimationsProvider.AnimationEndCallback) {
        if (entryAnimationPresented) {
            binding.mainContainer.post {
                animationsProvider.exitBottomAnimation(
                    binding.mainContainer,
                    callback
                )
            }
            entryAnimationPresented = false
        } else {
            callback.onAnimationEnd()
        }
    }
}