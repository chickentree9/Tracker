package com.example.tracker

import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

class SqlServerConnectionHelper {

    private var connection: Connection? = null

    fun connect(connectionString: String) {
        try {
            val connection = DriverManager.getConnection(connectionString)
            this.connection = connection
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun executeQuery(sql: String): ResultSet? {
        return connection?.createStatement()?.executeQuery(sql)?.let { it }
    }

    fun close() {
        try {
            if (connection != null) {
                connection!!.close()
                connection = null
            }
        } finally {
            connection = null
        }
    }
}