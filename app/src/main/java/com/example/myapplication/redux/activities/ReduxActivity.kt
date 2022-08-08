package com.example.myapplication.redux.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.myapplication.R
import com.example.myapplication.animations.AnimationsProvider
import com.example.myapplication.databinding.ActivityMainManualManagementBinding
import com.example.myapplication.redux.fragments.ReduxAboutFragment
import com.example.myapplication.redux.fragments.ReduxCartFragment
import com.example.myapplication.redux.fragments.ReduxStoreFragment
import com.example.myapplication.redux.implementation.AppStore
import com.example.myapplication.redux.implementation.FragmentType
import com.example.myapplication.redux.implementation.actions.CartActions
import com.example.myapplication.redux.implementation.actions.NavigationActions
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ReduxActivity : AppCompatActivity(), KoinComponent {

    val appStore: AppStore by inject()
    private lateinit var binding: ActivityMainManualManagementBinding
    private val storeFragment = ReduxStoreFragment()
    private val cartFragment = ReduxCartFragment()
    private val aboutFragment = ReduxAboutFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainManualManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Redux Store"

        appStore.dispatch(CartActions.RestoreProductsFromCache())

        supportFragmentManager.commit {
            add(R.id.mainContainer, storeFragment)
            add(R.id.mainContainer, cartFragment)
            add(R.id.mainContainer, aboutFragment)

            when (appStore.getState().navigationState.lastFragment) {
                FragmentType.STORE -> goToStore()
                FragmentType.CART -> goToCart()
                FragmentType.ABOUT -> goToAbout()
                FragmentType.BOTH -> {
                    goToCart()
                    goToAbout()
                }
            }
        }

        binding.tabBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.store_redux -> goToStore()
                R.id.cart_redux -> goToCart()
                R.id.about_redux -> goToAbout()
                else -> false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            exitAnimationAndClose()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        exitAnimationAndClose()
    }

    private fun exitAnimationAndClose() {
        if (appStore.getState().navigationState.lastFragment != FragmentType.STORE) {
            binding.tabBar.selectedItemId = R.id.store_redux
        } else {
            finish()
        }
    }

    private fun goToStore(): Boolean {
        supportFragmentManager.commit {
            aboutFragment.exitAnimation(object : AnimationsProvider.AnimationEndCallback {
                override fun onAnimationEnd() {
                    hide(aboutFragment)
                }
            })
            cartFragment.exitAnimation(object : AnimationsProvider.AnimationEndCallback {
                override fun onAnimationEnd() {
                    hide(cartFragment)
                }
            })
            appStore.dispatch(NavigationActions.SetLastFragment(FragmentType.STORE))
        }
        return true
    }

    private fun goToCart(): Boolean {
        supportFragmentManager.commit {
            show(cartFragment)
            cartFragment.entryAnimation()
            aboutFragment.exitAnimation(object : AnimationsProvider.AnimationEndCallback {
                override fun onAnimationEnd() {
                    hide(aboutFragment)
                    appStore.dispatch(NavigationActions.SetLastFragment(FragmentType.CART))
                }
            })
        }
        return true
    }

    private fun goToAbout(): Boolean {
        supportFragmentManager.commit {
            show(aboutFragment)
            aboutFragment.entryAnimation()
            appStore.dispatch(NavigationActions.SetLastFragment(FragmentType.ABOUT))
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}