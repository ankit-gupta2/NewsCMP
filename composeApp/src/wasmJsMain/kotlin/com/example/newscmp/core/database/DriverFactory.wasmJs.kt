package com.example.newscmp.core.database

import app.cash.sqldelight.db.SqlDriver

actual class DriverFactory {
    actual fun getDriver(): SqlDriver? {
        return null
    }
}