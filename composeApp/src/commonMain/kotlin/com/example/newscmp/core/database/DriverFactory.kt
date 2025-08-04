package com.example.newscmp.core.database

import app.cash.sqldelight.db.SqlDriver

expect class DriverFactory {
    fun getDriver() : SqlDriver?
}