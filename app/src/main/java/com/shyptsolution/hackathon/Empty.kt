package com.shyptsolution.hackathon

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast

class Empty : AppCompatActivity() {
    lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)
        val sharedPref=getSharedPreferences("diary", Context.MODE_PRIVATE)
        editor=sharedPref.edit()
        val accessToken=sharedPref.getString("accessToken","")
        val refreshToken=sharedPref.getString("refreshToken","")
        Handler(Looper.getMainLooper()).postDelayed({
            if(accessToken.isNullOrEmpty()){
                startActivity(Intent(this,Login::class.java))
            }
            else{
                startActivity(Intent(this,MainActivity::class.java))
            }
            finish()
        }, 1000)
    }
}