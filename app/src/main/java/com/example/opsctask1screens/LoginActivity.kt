package com.example.opsctask1screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.opsctask1screens.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if the user is already authenticated
        if (TokenManager.getAuthToken(this) != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("tblUsers")

        binding.btnLogin.setOnClickListener {
            val signupEmail = binding.txtEmail2.text.toString()
            val signupPassword = binding.txtPassword3.text.toString()

            if (signupEmail.isNotEmpty() && signupPassword.isNotEmpty()) {
                login(signupEmail, signupPassword)
            } else {
                Toast.makeText(this@LoginActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnReg.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            finish()
        }
    }

    private fun login(email: String, password: String) {
        databaseReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val userData = userSnapshot.getValue(UserDate::class.java)

                            if (userData != null && userData.password == password) {
                                // Check if the user has logged in before
                                val hasLoggedInBefore = TokenManager.hasLoggedInBefore(this@LoginActivity)

                                // Save the authentication token
                                TokenManager.saveAuthToken(this@LoginActivity, "your_generated_token_here")

                                Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()

                                if (hasLoggedInBefore) {
                                    // User has logged in before, navigate to the default fragment
                                    val defaultFragment = TokenManager.getDefaultFragment(this@LoginActivity)
                                    navigateToDefaultFragment(defaultFragment)
                                } else {
                                    // First-time login, navigate to the MainActivity
                                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                }

                                finish()
                                return
                            }
                        }
                    }
                    Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@LoginActivity, "Database Error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun navigateToDefaultFragment(defaultFragment: String?) {
        // Use the defaultFragment information to navigate to the desired fragment
        if (defaultFragment != null) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.putExtra("defaultFragment", defaultFragment)
            startActivity(intent)
        }
    }

    // Function to save the authentication token in SharedPreferences
    private fun saveAuthToken(context: Context, authToken: String) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("AuthToken", authToken)
        editor.apply()
    }
}
