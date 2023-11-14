package com.example.opsctask1screens


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.opsctask1screens.ObservationAdapter
import com.example.opsctask1screens.ObservationDetailsFragment
import com.example.opsctask1screens.ObservationModel


class MainActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //supportFragmentManager.beginTransaction()
        //      .replace(R.id.frag_container, FirstPage())
        //      .commit()
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.NavBar)

        if (intent.hasExtra("defaultFragment")) {
            val defaultFragment = intent.getStringExtra("defaultFragment")
            when (defaultFragment) {
                "HotspotFragment" -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frag_container, SecondPage(), "ObservationFragment")
                        .commit()
                }
                // Add more cases for other fragments as needed
            }
        } else {
            // If no default fragment specified, set the FirstPage() as the default
            supportFragmentManager.beginTransaction()
                .replace(R.id.frag_container, SecondPage(), "ObservationFragment")
                .commit()
        }

        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            val tag = menuItem.itemId.toString()
            val existingFragment = supportFragmentManager.findFragmentByTag(tag)

            if (existingFragment == null) {
                // Fragment not in the FragmentManager, replace it
                when (menuItem.itemId) {
                    R.id.id_birds -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frag_container, SecondPage(), tag)
                            .commit()
                        true
                    }

                    R.id.id_hotspot -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frag_container, FirstPage(), tag)
                            .commit()
                        true
                    }



                    R.id.id_more -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frag_container, MoreFragment(), tag)
                            .commit()
                        true
                    }

                    else -> false
                }
            } else {
                // Fragment is already in the FragmentManager, do nothing
                true
            }
        }


    }


}


