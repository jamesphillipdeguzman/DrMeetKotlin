package com.example

import java.sql.Connection
import java.sql.DriverManager

object Database {

    private val dbUser = System.getenv("DB_USER") ?: error("DB_USER is not set")
    private val dbPassword = System.getenv("DB_PASSWORD") ?: ""
    private val dbHost = System.getenv("DB_HOST") ?: error("DB_HOST is not set")
    private val dbPort = System.getenv("DB_PORT") ?: "3306"
    private val dbName = System.getenv("DB_NAME") ?: error("DB_NAME is not set")

    private val URL = "jdbc:mysql://$dbHost:$dbPort/$dbName?sslMode=REQUIRED"

    fun connect(): Connection {
        return DriverManager.getConnection(URL, dbUser, dbPassword)
    }
}