package com.example.newscmp.core.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.newscmp.AppDatabase
import java.util.Properties

actual class DriverFactory {
    actual fun getDriver(): SqlDriver? {
        return JdbcSqliteDriver("jdbc:sqlite:app.db", Properties(), AppDatabase.Schema)
    }
}