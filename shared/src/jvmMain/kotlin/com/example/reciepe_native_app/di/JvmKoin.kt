package com.example.reciepe_native_app.di

import com.example.reciepe_native_app.data.local.getDatabaseBuilder
import org.koin.dsl.module

val jvmPlatformModule = module {
    single { getDatabaseBuilder() }
}
