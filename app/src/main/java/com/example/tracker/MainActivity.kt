package com.example.tracker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class MainActivity : AppCompatActivity() {
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    @SuppressLint("AuthLeak")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            // Check if the username and password are valid.
            if (username.isEmpty() || password.isEmpty()) {
                // Show an error message.
                val message = "Please enter a username and password."
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Try to login the user.
            try {
                // Create a new Thread object.
                val thread = Thread(Runnable {
                    // Connect to the database.
                    val connection = SqlServerConnectionHelper()
                    connection.connect("jdbc:sqlserver://trackerzq.database.windows.net:1433;database=tracker;user=uoeno@trackerzq;password=Unyuns_89;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;")
                    Log.d("MainActivity", "Connected to the database.")


                    // Execute a SQL query to check if the username and password are valid.
                    val sql = "SELECT * FROM Logins WHERE Username = '$username' AND Password = '$password'"
                    val resultSet = connection.executeQuery(sql)

                    // If the user is found, start the MainActivity2.
                    resultSet?.let {
                        if (it.next()) {
                            val intent = Intent(this, MainActivity2::class.java)
                            startActivity(intent)
                        } else {
                            // Show an error message.
                            val message = "Invalid username or password."
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    // Close the connection.
                    connection.close()
                })

                // Start the Thread object.
                thread.start()
            } catch (e: SQLException) {
                // Show an error message.
                val message = "An error occurred while trying to login."
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
    }
    }
}
