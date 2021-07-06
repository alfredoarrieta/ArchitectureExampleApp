package com.example.myapplication.redux.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.commit

import com.example.myapplication.R
import com.example.myapplication.animations.AnimationsProvider
import com.example.myapplication.mvvm.viemodels.MVVMViewModel
import com.example.myapplication.redux.fragments.ReduxAboutFragment
import com.example.myapplication.redux.fragments.ReduxCartFragment
import com.example.myapplication.redux.fragments.ReduxStoreFragment
import com.example.myapplication.redux.implementation.AppStore
import com.example.myapplication.redux.implementation.FragmentType
import com.example.myapplication.redux.implementation.actions.CartActions
import com.example.myapplication.redux.implementation.actions.NavigationActions
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ReduxActivity : AppCompatActivity(), KoinComponent {

    val appStore: AppStore by inject()
    private val storeFragment = ReduxStoreFragment()
    private val cartFragment = ReduxCartFragment()
    private val aboutFragment = ReduxAboutFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Redux Store"

        appStore.dispatch(CartActions.RestoreProductsFromCache())

        mainContainer.removeAllViews()

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
        if(appStore.getState().navigationState.lastFragment != FragmentType.STORE) {
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
            appStore.dispatch(NavigationActions.SetLastFragment(FragmentType.STORE))
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
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    }
}