package com.example.reciepe_native_app.data.local

import java.nio.file.Path

internal fun resolveAppDataDir(appName: String = "RecipeBox"): Path {
    val os = System.getProperty("os.name").orEmpty().lowercase()
    val home = System.getProperty("user.home")
    return when {
        os.contains("win") -> {
            val roaming = System.getenv("APPDATA")
            if (!roaming.isNullOrBlank()) {
                Path.of(roaming, appName)
            } else {
                Path.of(home, "AppData", "Roaming", appName)
            }
        }
        os.contains("mac") -> Path.of(home, "Library", "Application Support", appName)
        else -> {
            val xdg = System.getenv("XDG_DATA_HOME")
            if (!xdg.isNullOrBlank()) {
                Path.of(xdg, appName)
            } else {
                Path.of(home, ".local", "share", appName)
            }
        }
    }
}
