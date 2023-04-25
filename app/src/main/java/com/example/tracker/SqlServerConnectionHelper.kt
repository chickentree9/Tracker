package com.example.tracker

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
        try {
            val statement = connection?.createStatement()
            return statement?.executeQuery(sql)
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            connection?.close()
        }
        return null
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