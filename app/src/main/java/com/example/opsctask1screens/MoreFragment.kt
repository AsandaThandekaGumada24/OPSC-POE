package com.example.opsctask1screens

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MoreFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_more, container, false)

        val nav = view.findViewById<NavigationView>(R.id.nav_view)
        nav.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
              /*  R.id.nav_game -> {
                    // load the FirstPage fragment
                    childFragmentManager.beginTransaction().replace(R.id.frag_container, FirstPage())
                        .commit()
                         childFragmentManager.popBackStack()
                    true
                }**/

                R.id.nav_observation -> {
                    // load the ObservationFragment fragment
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.frag_container, ObservationFragment())
                        ?.addToBackStack(null) // Add the transaction to the back stack
                        ?.commit()
                    true
                }
              /*  R.id.nav_account ->{
                    // load the hotspotFragment
                    childFragmentManager.beginTransaction().replace(R.id.frag_container, ObservationFragment())
                        .commit()
                         childFragmentManager.popBackStack()
                    true
                }*/
                R.id.nav_settings -> {
                    // load the FourthPage fragment
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.frag_container, FourthPage())
                        ?.addToBackStack(null) // Add the transaction to the back stack
                        ?.commit()
                    true
                }
                R.id.nav_about -> {
                    // load the MoreFragment fragment
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.frag_container, MoreFragment())
                        ?.addToBackStack(null) // Add the transaction to the back stack
                        ?.commit()
                    true
                }

                R.id.nav_logout -> {
                    // Clear the authentication token
                    TokenManager.clearAuthToken(requireContext())

                    // Redirect to login screen
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                    true
                }
                // Add more cases as needed for other navigation drawer items
                else -> false
            }

        }

        return view
    }


}