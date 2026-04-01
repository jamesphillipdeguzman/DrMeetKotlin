package com.example

import java.sql.Connection
import java.sql.DriverManager

object Database {
    private const val URL =
        "jdbc:mysql://drmeet-mysql-drmeet.h.aivencloud.com:12507/drmeet_db?sslMode=REQUIRED"

    private const val USER = "jamesphillipd"
    private const val PASSWORD = "AVNS_gVOdL2dDRDunTxzgxH2"

    fun connect(): Connection {
        return DriverManager.getConnection(URL, USER, PASSWORD)
    }
}