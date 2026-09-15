package com.example.reciepe_native_app.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import java.nio.file.Files

internal fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dir = resolveAppDataDir()
    Files.createDirectories(dir)
    val dbFile = dir.resolve(RECIPE_ROOM_DB_FILE_NAME).toFile()
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}
