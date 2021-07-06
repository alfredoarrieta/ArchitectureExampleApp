package com.example.myapplication.mvvm.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.example.myapplication.R
import com.example.myapplication.animations.AnimationsProvider
import com.example.myapplication.mvvm.fragments.MVVMAboutFragment
import com.example.myapplication.mvvm.fragments.MVVMCartFragment
import com.example.myapplication.mvvm.fragments.MVVMStoreFragment
import com.example.myapplication.mvvm.viemodels.MVVMViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MVVMActivity : AppCompatActivity() {

    val viewModel: MVVMViewModel by viewModels()
    private val storeFragment = MVVMStoreFragment()
    private val cartFragment = MVVMCartFragment()
    private val aboutFragment = MVVMAboutFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "MVVM Store"

        supportFragmentManager.commit {
            add(R.id.mainContainer, storeFragment)
            add(R.id.mainContainer, cartFragment)
            add(R.id.mainContainer, aboutFragment)

            when (viewModel.lastFragment) {
                MVVMViewModel.FragmentType.STORE -> goToStore()
                MVVMViewModel.FragmentType.CART -> goToCart()
                MVVMViewModel.FragmentType.ABOUT -> goToAbout()
                MVVMViewModel.FragmentType.BOTH -> {
                    goToCart()
                    goToAbout()
                }
            }
        }

        tabBar.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.store -> goToStore()
                R.id.cart -> goToCart()
                R.id.about -> goToAbout()
                else -> false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            exitAnimationAndClose()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        exitAnimationAndClose()
    }

    private fun exitAnimationAndClose() {
        if(viewModel.lastFragment != MVVMViewModel.FragmentType.STORE) {
            tabBar.selectedItemId = R.id.store
        }else {
            finish()
        }
    }

    private fun goToStore(): Boolean {
        supportFragmentManager.commit {
            aboutFragment.exitAnimation(object : AnimationsProvider.AnimationEndCallback {
                override fun onAnimationEnd() { hide(aboutFragment) }
            })
            cartFragment.exitAnimation(object : AnimationsProvider.AnimationEndCallback {
                override fun onAnimationEnd() { hide(cartFragment) }
            })
            viewModel.lastFragment = MVVMViewModel.FragmentType.STORE
        }
        return true
    }

    private fun goToCart(): Boolean {
        supportFragmentManager.commit {
            show(cartFragment)
            cartFragment.entryAnimation()
            aboutFragment.exitAnimation(object: AnimationsProvider.AnimationEndCallback {
                override fun onAnimationEnd() {
                    hide(aboutFragment)
                    viewModel.lastFragment = MVVMViewModel.FragmentType.CART
                }
            })
        }
        return true
    }

    private fun goToAbout(): Boolean {
        supportFragmentManager.commit {
            show(aboutFragment)
            aboutFragment.entryAnimation()
            if(viewModel.lastFragment == MVVMViewModel.FragmentType.CART){
                viewModel.lastFragment = MVVMViewModel.FragmentType.BOTH
            }else {
                viewModel.lastFragment = MVVMViewModel.FragmentType.ABOUT
            }
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    }
}