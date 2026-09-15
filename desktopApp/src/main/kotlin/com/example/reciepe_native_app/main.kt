package com.example.reciepe_native_app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.example.reciepe_native_app.di.initKoin
import com.example.reciepe_native_app.di.jvmPlatformModule

fun main() {
    initKoin(extraModules = listOf(jvmPlatformModule))
    application {
        val windowState = rememberWindowState(size = DpSize(1100.dp, 760.dp))
        Window(
            onCloseRequest = ::exitApplication,
            title = "RecipeBox",
            state = windowState,
        ) {
            RecipeBoxDesktopTheme {
                DesktopRoot()
            }
        }
    }
}

private val LightColors = lightColorScheme(
    primary = Color(0xFFB54714),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDBCB),
    secondary = Color(0xFF2F6A4A),
    background = Color(0xFFFFF8F5),
    surface = Color(0xFFFFF8F5),
)

@Composable
fun RecipeBoxDesktopTheme(content: @Composable () -> Unit) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components { add(KtorNetworkFetcherFactory()) }
            .build()
    }
    MaterialTheme(colorScheme = LightColors, content = content)
}
