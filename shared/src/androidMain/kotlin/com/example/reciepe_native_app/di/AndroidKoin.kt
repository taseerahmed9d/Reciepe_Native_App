package com.example.reciepe_native_app.di

import android.content.Context
import com.example.reciepe_native_app.data.local.getDatabaseBuilder
import org.koin.dsl.module

fun androidPlatformModule(context: Context) = module {
    single { getDatabaseBuilder(context) }
}
