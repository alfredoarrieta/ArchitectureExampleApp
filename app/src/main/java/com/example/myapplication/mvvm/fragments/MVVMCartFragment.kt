package com.example.myapplication.mvvm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapters.CartProductAdapter
import com.example.myapplication.adapters.CartProductInterface
import com.example.myapplication.data.model.Product
import com.example.myapplication.databinding.FragmentCartBinding
import com.example.myapplication.mvvm.viemodels.MVVMViewModel

class MVVMCartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MVVMViewModel by activityViewModels()

        viewModel.onDataChanged.observe(this, { data ->
            binding.checkoutButton.text = String.format("Cart Total $%.2f",data.cartTotal)
            (binding.productList.adapter as CartProductAdapter).setProducts(data.cart)
        })

        binding.productList.layoutManager = LinearLayoutManager(context)
        binding.productList.adapter = CartProductAdapter(object : CartProductInterface {
            override fun onProductDeleted(product: Product) {
                viewModel.removeProductFromCart(product)
            }
        })
    }
}