package com.example.myapplication.mvvm.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.myapplication.R
import com.example.myapplication.mvvm.viemodels.MVVMViewModel
import kotlinx.android.synthetic.main.activity_main_nav_component.*


class MVVMActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_nav_component)

        supportActionBar?.title = "MVVM Store"

        tabBar.inflateMenu(R.menu.tabbar_menu_mvvm)

        val navController = Navigation.findNavController(this, R.id.mainContainer)
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph_mvvm)
        navGraph.setStartDestination(R.id.store_mvvm)
        navController.setGraph(navGraph,null)

        val viewModel: MVVMViewModel by viewModels()

        tabBar.setOnItemSelectedListener { item ->
            val navBuilder = NavOptions.Builder();
            when(item.itemId){
                R.id.store_mvvm -> /*goToStore()*/ {
                    navBuilder.setEnterAnim(R.anim.slide_in_left).setExitAnim(R.anim.slide_out_right)
                    navController.navigate(R.id.store_mvvm, null, navBuilder.build())
                    viewModel.lastFragment = MVVMViewModel.FragmentType.STORE
                    true
                }
                R.id.cart_mvvm -> /*goToCart()*/{
                    if(viewModel.lastFragment == MVVMViewModel.FragmentType.STORE){
                        navBuilder.setEnterAnim(R.anim.slide_in_right).setExitAnim(R.anim.slide_out_left)
                    }else{
                        navBuilder.setEnterAnim(R.anim.slide_in_left).setExitAnim(R.anim.slide_out_right)
                    }
                    navController.navigate(R.id.cart_mvvm, null, navBuilder.build())
                    viewModel.lastFragment = MVVMViewModel.FragmentType.CART
                    true
                }
                R.id.about_mvvm -> /*goToAbout()*/ {
                    navBuilder.setEnterAnim(R.anim.slide_in_right).setExitAnim(R.anim.slide_out_left)
                    navController.navigate(R.id.about_mvvm, null, navBuilder.build())
                    viewModel.lastFragment = MVVMViewModel.FragmentType.ABOUT
                    true
                }
                else -> false
            }
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    }
}