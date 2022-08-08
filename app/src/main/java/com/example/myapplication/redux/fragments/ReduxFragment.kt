package com.example.myapplication.redux.fragments

import androidx.fragment.app.Fragment
import com.example.myapplication.redux.implementation.AppStore
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class ReduxFragment : Fragment(), KoinComponent {

    val appStore: AppStore by inject()

    val subscriptions = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }
}