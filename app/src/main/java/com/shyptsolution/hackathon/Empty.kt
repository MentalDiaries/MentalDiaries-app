package com.shyptsolution.hackathon

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.create


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

    fun check(){
        val client = OkHttpClient().newBuilder()
            .build()
        val mediaType = "application/json".toMediaTypeOrNull()
        val body: RequestBody = create(
            mediaType,
            "{\r\n    \"username\": \"username1\",\r\n    \"entry\": \"I am fucking depressed\",\r\n    \"entry_title\": \"sad hackathon round\"\r\n}"
        )
        val request: Request = Request.Builder()
            .url("https://mental-diaries.herokuapp.com/api/diary/entry/")
            .method("POST", body)
            .addHeader(
                "Authorization",
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjQ1OTA4MTgwLCJpYXQiOjE2NDU5MDYzODAsImp0aSI6ImMxNGU2MDhhYjIxZjQxMmNhOTUzYzEzMjg1ZmIxYTQ4IiwidXNlcl9pZCI6N30.xlrUByLxs9h3hk_6wilUFDPu4GzFIgn-vHcnxOcDQxY"
            )
            .addHeader("Content-Type", "application/json")
            .build()
        val response: Response = client.newCall(request).execute()
    }
}