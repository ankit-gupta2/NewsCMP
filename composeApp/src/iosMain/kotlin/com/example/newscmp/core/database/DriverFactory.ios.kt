package com.example.newscmp.core.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.newscmp.AppDatabase

actual class DriverFactory {
    actual fun getDriver(): SqlDriver? {
        return NativeSqliteDriver(schema = AppDatabase.Schema, name = "app.db")
    }
}