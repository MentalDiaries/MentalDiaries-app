package com.shyptsolution.hackathon

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject


class Login : AppCompatActivity() {
    lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        val sharedPref=getSharedPreferences("diary", Context.MODE_PRIVATE)
        editor=sharedPref.edit()
        val loginButt=findViewById<TextView>(R.id.login)
        loginButt.setOnClickListener {
            val userName=findViewById<EditText>(R.id.userName_input).text.toString()
            val password=findViewById<EditText>(R.id.password_input).text.toString()
            if(userName.isNotBlank() && password.isNotBlank()){
                val url="https://mental-diaries.herokuapp.com/api/users/login/"
                    val `object` = JSONObject()
                    val client = OkHttpClient().newBuilder()
                        .build()
                    val mediaType: MediaType = "text/plain".toMediaTypeOrNull()!!
                    val body = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("username", "raunit")
                        .addFormDataPart("password", "raunit")
                        .build()
                    val request: okhttp3.Request = okhttp3.Request.Builder()
                        .url("https://mental-diaries.herokuapp.com/api/users/login/")
                        .method("POST", body)
                        .build()
                    GlobalScope.launch {
                        val response = client.newCall(request).execute()
                        val message=response.message
                        if(message.toString()!="OK"){
                            withContext(Dispatchers.Main){
                                Toast.makeText(this@Login,"Some error Occurred",Toast.LENGTH_LONG).show()
                            }
                        }
                        else{
                            val responseObj=response.body?.string()
                            val Jobject = JSONObject(responseObj)
                          val accessToken=Jobject.get("access")
                          val refreshToken=Jobject.get("refresh")
                            editor.apply {
                                putString("accessToken",accessToken.toString())
                                putString("refreshToken",refreshToken.toString())
                            }
                            withContext(Dispatchers.Main){
                                Toast.makeText(this@Login,"Login Successful",Toast.LENGTH_LONG).show()
                                startActivity(Intent(this@Login,MainActivity::class.java))
                            }
                        }

                    }
            }
            else{
                Toast.makeText(this,"Check Username or Password.",Toast.LENGTH_LONG).show()
            }
        }

        val clickToRegister=findViewById<TextView>(R.id.heretoRegister)
        clickToRegister.setOnClickListener {
            var inflate = LayoutInflater.from(this)
            var popupview = inflate.inflate(R.layout.register, null, false)
            var builder = PopupWindow(
                popupview,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true
            )
            var closeReg=popupview.findViewById<ImageView>(R.id.closeRegister)
            closeReg.setOnClickListener { builder.dismiss() }

            builder.setBackgroundDrawable(getDrawable(R.drawable.popupback))
            builder.animationStyle=R.style.DialogAnimation
            builder.showAtLocation(
                this.findViewById(R.id.activitylogin),
                Gravity.CENTER,
                0,
                0
            )

            val Username=popupview.findViewById<EditText>(R.id.registerUsername)
            val Password=popupview.findViewById<EditText>(R.id.registerPass)
            val confirmPass=popupview.findViewById<EditText>(R.id.registerPassConfirm)
            val registerButt=popupview.findViewById<TextView>(R.id.register)
            registerButt.setOnClickListener {
            var finalPassword=""
                if (Password.text.toString()==confirmPass.text.toString()){
                    finalPassword=confirmPass.text.toString()
                    val url="https://mental-diaries.herokuapp.com/api/users/register/"
                    val `object` = JSONObject()
                    val client = OkHttpClient().newBuilder()
                        .build()
                    val mediaType: MediaType = "text/plain".toMediaTypeOrNull()!!
                    val body = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("username", "check")
                        .addFormDataPart("password", "raunit")
                        .build()
                    val request: okhttp3.Request = okhttp3.Request.Builder()
                        .url("https://mental-diaries.herokuapp.com/api/users/register/")
                        .method("POST", body)
                        .build()
                    GlobalScope.launch {
                        val response = client.newCall(request).execute()
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@Login,response.toString(),Toast.LENGTH_LONG).show()

                        }
                    }

                }
                else{
                    Toast.makeText(this@Login,"Please check your password. Password & Confirm Password doesn't match.",Toast.LENGTH_LONG).show()
                }
                if(Username.text.toString().isNotBlank()&& finalPassword.isNotBlank()){

                }
            }

            }
    }
}