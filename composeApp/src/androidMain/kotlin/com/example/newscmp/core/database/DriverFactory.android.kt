package com.example.newscmp.core.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.newscmp.AppDatabase

actual class DriverFactory(private val context: Context) {
    actual fun getDriver(): SqlDriver? {
        return AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = context,
            name = "app.db"
        )
    }
}