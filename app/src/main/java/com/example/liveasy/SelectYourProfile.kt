package com.example.liveasy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SelectYourProfile : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private  lateinit var signout1: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_your_profile)

        auth= FirebaseAuth.getInstance()
        signout1 = findViewById(R.id.signout1)

        signout1.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LanguageActivity_final::class.java))
        }
    }
}