package com.example.tracker

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationListener
import android.Manifest
import android.content.Intent
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


class MainActivity2 : AppCompatActivity() {

    //Location Tracker variables
    private lateinit var switchTracker: Switch
    private lateinit var locationManager: LocationManager
    private var locationListener: LocationListener? = null

    //Timer variables
    lateinit var timer: CountDownTimer
    var startTime: Long = 0
    var elapsedTime: Long = 0
    private lateinit var textViewTime: TextView

    private fun Any.removeUpdates(locationListener: LocationListener?) {
    }

    private fun Any.requestLocationUpdates(gpsProvider: String, i: Int, fl: Float, locationListener: LocationListener) {
    }

    //Method to Start Location updates
    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationListener = LocationListener { location -> updateLocation(location) }

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                30000,
                0f,
                locationListener!!
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
    }

    //Method to Stop Location updates
    private fun stopLocationUpdates() {
        locationManager.removeUpdates(locationListener)
        locationListener = null

    }

    //Variable to store the last location
    private var lastLocation: Location? = null

    //Method to update the location in the database
    //This method is called every time the location changes
    private fun updateLocation(location: Location) {
        val dbHelper = SqlServerConnectionHelper() // Instantiate your database helper class

        val query = "UPDATE LocationTable SET latitude = ${location.latitude}, longitude = ${location.longitude} WHERE id = 1"

        dbHelper.executeQuery(query) // Execute the UpdateLocation query
    }

    //Method to format the time
    fun formatElapsedTime(time: Long): String {
        val hours = time / 3600000
        val minutes = (time % 3600000) / 60000
        val seconds = (time % 60000) / 1000
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }


    //Method to save the time to the database
    fun saveTime(time: Long) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // Initialize the text view
        textViewTime = findViewById(R.id.textViewTime)


        switchTracker = findViewById(R.id.switchTracker)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


        //Process the switch change to track location and send location updates to the database
        switchTracker.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startLocationUpdates()
                startTime = System.currentTimeMillis()
                timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        elapsedTime = System.currentTimeMillis() - startTime
                        val formattedTime = formatElapsedTime(elapsedTime)
                        textViewTime.text = formattedTime
                        //

                    }
                    override fun onFinish() {}
                }
                timer.start()
            } else {
                stopLocationUpdates()
                timer.cancel()
                val formattedTime = formatElapsedTime(elapsedTime)
                textViewTime.text = formattedTime
            }
        }

        // Initialize the Save Button to save the time
        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            // Save the time if formatted time is greater than 0
            if (elapsedTime > 0) {
                val formattedTime = formatElapsedTime(elapsedTime)
                val currentTime = System.currentTimeMillis()
                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                val resultDate = Date(currentTime)
                val formattedDate = sdf.format(resultDate)
                val time = Time(formattedDate, formattedTime)

                // Insert the time into the database Must create Database and Dao
                // MyDatabase.getInstance(this).timeDao().insert(time)
            }

        }

        // Initialize the Account Information Button to go to the Account Activity
        val accountButton = findViewById<Button>(R.id.accInfoButton)
        accountButton.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }

    }


    private fun Time(formattedDate: String, formattedTime: String): Any {
        TODO("Not yet implemented")
    }
}





