package com.shyptsolution.hackathon

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        val sharedPref=getSharedPreferences("diary", Context.MODE_PRIVATE)

        val loginButt=findViewById<TextView>(R.id.login)
        loginButt.setOnClickListener {
            val userName=findViewById<EditText>(R.id.userName_input).text.toString()
            val password=findViewById<EditText>(R.id.password_input).text.toString()
            if(userName.isNotBlank() && password.isNotBlank()){
//                val client = OkHttpClient().newBuilder()
//                    .build()
//                val mediaType: MediaType = "text/plain".toMediaTypeOrNull()!!
//                val body: RequestBody = FormBody.Builder.
//                    .addFormDataPart("username", "raunit")
//                    .addFormDataPart("password", "raunit")
//                    .build()
//                val request: Request = Request.Builder()
//                    .url("main/api/users/register/")
//                    .method("POST", body)
//                    .build()
//                val response: Response = client.newCall(request).execute()
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