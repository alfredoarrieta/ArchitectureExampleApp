package com.example.myapplication.mvvm.fragments

import androidx.fragment.app.Fragment
import com.example.myapplication.mvvm.activities.MVVMActivity
import com.example.myapplication.mvvm.viemodels.MVVMViewModel
import org.koin.core.component.KoinComponent

abstract class MVVMFragment: Fragment(), KoinComponent {

    val mainViewModel: MVVMViewModel get() = (activity as MVVMActivity).viewModel

    val mainActivity: MVVMActivity get() = (activity as MVVMActivity)
}