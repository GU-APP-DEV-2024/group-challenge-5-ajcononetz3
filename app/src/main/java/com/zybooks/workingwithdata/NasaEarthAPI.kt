package com.zybooks.workingwithdata

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.zybooks.workingwithdata.BuildConfig
import org.json.JSONObject

class NasaEarthAPI : AppCompatActivity() {
    lateinit var latTextView: TextView
    lateinit var latEditText: EditText
    lateinit var longTextView: TextView
    lateinit var longEditText: EditText
    lateinit var dimTextView: TextView
    lateinit var dimEditText: EditText
    lateinit var dateTextView: TextView
    lateinit var dateEditText: EditText

    lateinit var recyclerView: RecyclerView
    lateinit var imageDataSet: ArrayList<ImageData>
    lateinit var imageCustomAdapter: ImageCustomAdapter

//    data class ImageData(val url: String, val description: String = "", val date: String = "") {
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nasa_earth_api)

        supportActionBar?.title = "Working with NASA"


        latTextView = findViewById(R.id.latTextView)
        latEditText = findViewById(R.id.latEditText)

        longTextView = findViewById(R.id.longTextView)
        longEditText = findViewById(R.id.longEditText)

        dimTextView = findViewById(R.id.dimTextView)
        dimEditText = findViewById(R.id.dimEditText)

        dateTextView = findViewById(R.id.dateTextView)
        dateEditText = findViewById(R.id.dateEditText)

        val searchButton: Button = findViewById(R.id.searchButton)
        searchButton.setOnClickListener {
            searchEarthRequest()
        }

        val clearButton: Button = findViewById(R.id.clearButton)
        clearButton.setOnClickListener {
            clearEditTextFields()
        }

        imageDataSet = arrayListOf(ImageData("https://api.nasa.gov/assets/img/general/houston_2.png",""))
        imageCustomAdapter = ImageCustomAdapter(imageDataSet)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = imageCustomAdapter


    }

    // Hardcoded version
    private fun searchEarth() {
        val hardCodedInfo = "https://api.nasa.gov/planetary/earth/assets?lon=$longEditText&lat=$latEditText&date=$dateEditText&dim=$dimEditText&api_key=${BuildConfig.NASA_API_KEY}"
    }

    // Create and make earth request
    private fun searchEarthRequest() {
        // Building URL for request.
        var base_url = "https://api.nasa.gov/planetary/earth/assets?"

        // Long
        if (longEditText.text.isNotEmpty()) {
            var lon = longEditText.text.toString().toDoubleOrNull()
            if (lon != null) {
                base_url += "lon=$lon"
            }
        } else {
            Toast.makeText(this, "Please provide a longitude", Toast.LENGTH_LONG).show()
            return
        }

        // Lat
        if (latEditText.text.isNotEmpty()) {
            var lat = latEditText.text.toString().toDoubleOrNull()
            if (lat != null) {
                base_url += "&lat=$lat"
            }
        } else {
            Toast.makeText(this, "Please provide a latitude", Toast.LENGTH_LONG).show()
            return
        }

        // Date
        if (dateEditText.text.isNotEmpty()) {
            var date = dateEditText.text.toString()
            base_url += "&date=$date"
        }

        // Dim
        if (dimEditText.text.isNotEmpty()) {
            var dim = dimEditText.text.toString()
            base_url += "&dim=$dim"
        }

        var url = base_url +
                "&api_key=${BuildConfig.NASA_API_KEY}"

        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)

        val requestObj = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response -> processRequest(response) },
            { error -> Log.d(TAG, "Error: $error")
            Toast.makeText(this, "No image available", Toast.LENGTH_LONG).show()})

        queue.add(requestObj)
    }

    // Process earth request
    private fun processRequest(response: JSONObject) {
        Log.d(TAG, response.toString())
        var url = response.getString("url")
        var date = response.getString("date")
        imageDataSet.clear()
        imageDataSet.add(ImageData(url, "", date))
        imageCustomAdapter.notifyDataSetChanged()
    }


    private fun clearEditTextFields() {
        latEditText.text.clear()
        longEditText.text.clear()
        dimEditText.text.clear()
        dateEditText.text.clear()
    }
}