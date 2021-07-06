package com.example.myapplication.data.di

import android.content.Context
import com.example.myapplication.animations.AnimationsProvider
import com.example.myapplication.data.datasources.LocalDataSource
import com.example.myapplication.mvvm.repositories.StoreRepository
import com.example.myapplication.redux.implementation.AppStore
import com.google.gson.Gson
import org.koin.core.module.Module
import org.koin.dsl.module

class ModuleProvider(private val context: Context) {

    private val persistenceModules = module {
        single { context }
        single { Gson() }
        single { LocalDataSource(get(),get()) }
    }

    private val mvvmModules = module {
        single { StoreRepository(get()) }
    }

    private val reduxModules = module{
        single { AppStore() }
    }

    private val animationModules = module{
        single { AnimationsProvider() }
    }

    fun getModules(): List<Module>{
        return listOf(persistenceModules, reduxModules, mvvmModules, animationModules)
    }

}