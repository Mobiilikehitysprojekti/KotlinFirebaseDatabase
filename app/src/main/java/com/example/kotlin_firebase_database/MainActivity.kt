package com.example.kotlin_firebase_database

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(HomeFragment())
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.message -> {
                    loadFragment(ChatFragment())
                    true
                }
                R.id.settings -> {
                    loadFragment(SettingFragment())
                    true
                }
                else -> {false}
            }
        }

    }

    fun addUser(v: View?) {
        val name = findViewById<EditText>(R.id.name).text.toString()
        val email = findViewById<EditText>(R.id.email).text.toString()
        val userid = findViewById<EditText>(R.id.userid).text.toString()
        writeNewUser(userid,name,email)
    }
    fun findUser(v: View?) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        val text = findViewById<EditText>(R.id.showText)
        val name = findViewById<EditText>(R.id.searchName).text.toString()
        database.child("users").child(name).get().addOnSuccessListener {
            val email =  it.child("email").value
            text.setText(email.toString())
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

    }

    private fun writeNewUser(userId: String, name: String, email: String) {
        val user = User(name, email)
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child("users").child(userId).setValue(user)
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

}