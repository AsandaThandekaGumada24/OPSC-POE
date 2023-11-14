package com.example.opsctask1screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import kotlin.math.*

class ObservationDetailsFragment : Fragment() {

    companion object {
        private const val ARG_OBSERVATION = "observation"

        fun newInstance(observation: ObservationModel?): ObservationDetailsFragment {
            val fragment = ObservationDetailsFragment()
            val args = Bundle()
            args.putParcelable(ARG_OBSERVATION, observation)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_observation_details, container, false)

        // Retrieve observation details from arguments
        val observation: ObservationModel? = arguments?.getParcelable(ARG_OBSERVATION)
            ?: ObservationModel("", "", "", "", "", "", "", false, false, false, 0, 0.0, 0.0, "")

        // Display observation details in TextViews
        view.findViewById<TextView>(R.id.detailsSpeciesTextView).text =
            "Species: ${observation?.comName.orEmpty()}"
        view.findViewById<TextView>(R.id.detailsLocationTextView).text =
            "Location: ${observation?.locName.orEmpty()}"

        // Calculate distance
        val userLocation = getUserLocation()
        val distance = calculateDistance(
            userLocation.latitude, userLocation.longitude,
            observation?.latitude ?: 0.0, observation?.longitude ?: 0.0
        )

        // Display distance in TextView
        view.findViewById<TextView>(R.id.distanceTextView).text = "Distance: $distance km"

        // Add more TextViews and set their text based on observation details

        return view
    }

    private fun getUserLocation(): LatLng {
        // Assume this function returns the user's location as LatLng
        // You can replace it with the actual logic to get the user's location
        // For example, use the FusedLocationProviderClient as shown in the SecondPage fragment
        return LatLng(0.0, 0.0) // Default location (replace with actual location)
    }

    // Add the setDistance method if needed
    fun setDistance(distance: Float) {
        // Implement the logic to set the distance in your fragment
        // For example, update a TextView with the distance
        val distanceTextView = view?.findViewById<TextView>(R.id.distanceTextView)
        distanceTextView?.text = "Distance: $distance km"
    }

    // Add your distance calculation function here
    private fun calculateDistance(
        userLat: Double, userLng: Double,
        observationLat: Double, observationLng: Double
    ): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            userLat, userLng,
            observationLat, observationLng,
            results
        )
        return results[0]
    }
}
