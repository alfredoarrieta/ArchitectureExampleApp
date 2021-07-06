package com.example.myapplication

import android.support.multidex.MultiDexApplication
import com.example.myapplication.data.di.ModuleProvider
import org.koin.core.context.startKoin

class MainApplication: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin { modules(ModuleProvider(applicationContext).getModules()) }
    }

}