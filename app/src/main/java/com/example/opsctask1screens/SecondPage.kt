package com.example.opsctask1screens

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import okhttp3.OkHttpClient

class SecondPage : Fragment(), ObservationAdapter.OnItemClickListener {
    private val client = OkHttpClient.Builder().build()
    private val observations = mutableListOf<ObservationModel>()
    private lateinit var adapter: ObservationAdapter
    private lateinit var searchView: SearchView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var observation: ObservationModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (TokenManager.getAuthToken(requireContext()) == null) {
            // User not authenticated, navigate to login
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
            return null
        }

        val view = inflater.inflate(R.layout.fragment_second_page, container, false)

        // Initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Check and request location permission
        checkLocationPermission()

        val recyclerView = view.findViewById<RecyclerView>(R.id.observationRecyclerView)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        adapter = ObservationAdapter(observations, this)
        recyclerView.adapter = adapter

        // Fetch data from the API and update the observations list
        fetchData()
        getUserLocation()

        // Set up the SearchView
        searchView = view.findViewById(R.id.search)
        // Add a listener for the text changes in the search view
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle query submission if needed
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter the data based on the search query
                adapter.filter.filter(newText)
                return false
            }
        })

        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted, perform the location-related task if needed
                    // In this case, fetching the user's location and updating the UI
                    fetchUserLocationAndHandleClick()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onItemClick(observation: ObservationModel) {
        // Handle the item click event
        // For example, show details in a new fragment
        this.observation = observation  // Set the observation property
        fetchUserLocationAndHandleClick()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted, proceed with location detection
            detectUserLocation()
        }
    }

    private fun detectUserLocation() {
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10000) // Update every 10 seconds (adjust as needed)

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val lastLocation = locationResult.lastLocation
                // Update userLocation if needed
            }
        }

        // Request location updates
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun fetchUserLocationAndHandleClick() {
        // Check if location permissions are granted
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Location permissions are not granted, request them
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Location permissions are granted, fetch the user's location
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val userLocation = LatLng(location.latitude, location.longitude)

                    // Ensure observation is not null before proceeding
                    observation?.let { obs ->
                        // Calculate distance
                        val distance = calculateDistance(
                            userLocation.latitude, userLocation.longitude,
                            obs.latitude, obs.longitude
                        )

                        // Pass the distance to the details fragment
                        val detailsFragment = ObservationDetailsFragment.newInstance(obs)
                        detailsFragment.setDistance(distance)

                        // Use FragmentManager to replace the current fragment with detailsFragment
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.frag_container, detailsFragment)
                            .addToBackStack(null) // Optional: Add to back stack for navigation
                            .commit()
                    }
                }
            }
        }
    }

    private fun fetchData() {
        val request = Request.Builder()
            .url("https://api.ebird.org/v2/data/obs/ZA-EC/recent")
            .get()
            .addHeader("X-eBirdApiToken", "6gclai1l9b4t")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (isAdded) { // Check if the fragment is attached to an activity
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()

                        responseData?.let {
                            // Parse the JSON data and update the observations list
                            parseObservations(responseData)

                            // Notify the adapter that the data has changed
                            requireActivity().runOnUiThread {
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        })
    }

    private fun parseObservations(responseData: String) {
        try {
            val jsonArray = JSONArray(responseData)
            for (i in 0 until jsonArray.length()) {
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)

                // Check for the existence of required fields
                val subId = if (jsonObject.has("subId")) jsonObject.getString("subId") else ""
                val speciesCode =
                    if (jsonObject.has("speciesCode")) jsonObject.getString("speciesCode") else ""
                val comName = if (jsonObject.has("comName")) jsonObject.getString("comName") else ""
                val sciName = if (jsonObject.has("sciName")) jsonObject.getString("sciName") else ""
                val locName = if (jsonObject.has("locName")) jsonObject.getString("locName") else ""
                val locId = if (jsonObject.has("locId")) jsonObject.getString("locId") else ""

                // Handle optional fields gracefully
                val obsName = if (jsonObject.has("obsName")) jsonObject.getString("obsName") else ""
                val obsValid =
                    if (jsonObject.has("obsValid")) jsonObject.getBoolean("obsValid") else false
                val obsReviewed =
                    if (jsonObject.has("obsReviewed")) jsonObject.getBoolean("obsReviewed") else false
                val locationPrivate =
                    if (jsonObject.has("locationPrivate")) jsonObject.getBoolean("locationPrivate") else false
                val howMany = if (jsonObject.has("howMany")) jsonObject.getInt("howMany") else 0
                val lat = if (jsonObject.has("lat")) jsonObject.getDouble("lat") else 0.0
                val lng = if (jsonObject.has("lng")) jsonObject.getDouble("lng") else 0.0

                // Create the ObservationModel
                val observation = ObservationModel(
                    subId, speciesCode, comName, sciName, locName, locId, obsName,
                    obsValid, obsReviewed, locationPrivate, howMany, lat, lng
                )

                // Add the observation to the list
                observations.add(observation)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun calculateDistance(
        userLat: Double, userLng: Double,
        observationLat: Double, observationLng: Double
    ): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            userLat, userLng,
            observationLat, observationLng,
            results
        )
        return results[0]
    }

    private fun getUserLocation() {
        // Check if location permissions are granted
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Location permissions are not granted, request them
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Location permissions are granted, fetch the user's location
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val userLocation = LatLng(location.latitude, location.longitude)
                    // You can use the userLocation if needed
                }
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
