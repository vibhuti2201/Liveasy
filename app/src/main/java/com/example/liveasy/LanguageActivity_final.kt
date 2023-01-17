package com.example.liveasy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class LanguageActivity_final : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_final)


        findViewById<Button>(R.id.next1).setOnClickListener{
            Intent(this, OTPVerificationActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}