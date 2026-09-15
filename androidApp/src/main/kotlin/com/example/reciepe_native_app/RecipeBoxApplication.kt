package com.example.reciepe_native_app

import android.app.Application
import com.example.reciepe_native_app.di.androidPlatformModule
import com.example.reciepe_native_app.di.initKoin

class RecipeBoxApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            extraModules = listOf(
                androidPlatformModule(this),
            ),
        )
    }
}
