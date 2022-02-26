package com.shyptsolution.hackathon

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationView
import com.shyptsolution.classproject.DataBase.DiaryEntity
import com.shyptsolution.classproject.DataBase.DiaryViewModel
import com.shyptsolution.hackathon.RecyclerView.APi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var viewModel: DiaryViewModel
    lateinit var title: EditText
    lateinit var diaryNote: EditText
    lateinit var sharedPref:SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    @SuppressLint("CommitPrefEdits", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.gradient))
        //SharedPreference
        sharedPref = getSharedPreferences("diary", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        refreshToken(sharedPref)
        setContentView(R.layout.activity_main)

        //Title and Diary Note
        title = findViewById<EditText>(R.id.diaryTitle)
        diaryNote = findViewById<EditText>(R.id.diaryNote)


        //Menu Options
        val navigationView = findViewById<NavigationView>(R.id.navmenu)
        val navheader = navigationView.getHeaderView(0)
        val username = navheader.findViewById<TextView>(R.id.userName)
        username.setText("Welcome, " + sharedPref.getString("username", ""))
        val driver: DrawerLayout = findViewById(R.id.drawerlayout)
//        supportActionBar?.hide()
        toggle = ActionBarDrawerToggle(this, driver, R.string.open, R.string.close)
        driver.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //On item click menu
        navigationView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.analysis -> {
                    driver.closeDrawer(GravityCompat.START)

                }

                R.id.appointment -> {

                }

                R.id.totalDiary -> {
                    startActivity(Intent(this, AllDiary::class.java))
                    driver.closeDrawer(GravityCompat.START)

                }
                R.id.logout -> {
                    editor.clear().apply()
                    startActivity(Intent(this, Login::class.java))
                    GlobalScope.launch(Dispatchers.IO) {
                        viewModel.repository.deleteAll()
                    }
                    driver.closeDrawer(GravityCompat.START)
                }

                R.id.exit -> {
                    finishAffinity()
                }

//               else -> throw IllegalStateException("Unexpected value: " + item.itemId)
            }

            true
        })


        //ViewModel and DataBase
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[DiaryViewModel::class.java]

        title.setText(sharedPref.getString("title", "").toString())
        diaryNote.setText(sharedPref.getString("lastDiary", "").toString())


    }

    @SuppressLint("SimpleDateFormat")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return when (item.itemId) {
            R.id.saveDiary -> {
                lifecycleScope.launch {
                    val titleText = title.text.toString()
                    val todayDiary = diaryNote.text.toString()
                    val df = SimpleDateFormat("dd-MM-yyyy, hh:mm aa")
                    var localDate = df.format(Date())
                    val id = Calendar.getInstance().timeInMillis.toString()
                    val Diary = DiaryEntity(id, titleText, localDate, todayDiary, "Status")
                    viewModel.repository.insert(Diary)
                    insertToServer(sharedPref,todayDiary,titleText)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Saved", Toast.LENGTH_LONG).show()
                        title.text.clear()
                        diaryNote.text.clear()
                    }
                }
                true

            }
            else -> false
        }

    }

//    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.savemenu,menu)
//        return super.onPrepareOptionsMenu(menu)
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.savemenu, menu)
        val saveItem = menu.findItem(R.id.saveDiary)

        if (saveItem != null) {
            saveItem.isVisible = false

        }
        if (title.text.toString().isNotBlank() && diaryNote.text.toString().isNotBlank()) {
            saveItem.setVisible(true)
        }
        title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                saveItem.isVisible = title.text.isNotBlank() || diaryNote.text.isNotBlank()
                editor.apply {
                    putString("title", title.text.toString())
                    apply()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                saveItem.isVisible = title.text.isNotBlank() || diaryNote.text.isNotBlank()
                editor.apply {
                    putString("title", title.text.toString())
                    apply()
                }
            }

        })
        diaryNote.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                saveItem.isVisible = title.text.isNotBlank() || diaryNote.text.isNotBlank()
                editor.apply {
                    putString("lastDiary", diaryNote.text.toString())
                    apply()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                saveItem.isVisible = title.text.isNotBlank() || diaryNote.text.isNotBlank()
                editor.apply {
                    putString("lastDiary", diaryNote.text.toString())
                    apply()
                }
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun refreshToken(sharedPreferences: SharedPreferences) {
        val `object` = JSONObject()
        val client = OkHttpClient().newBuilder()
            .build()
        val mediaType: MediaType = "text/plain".toMediaTypeOrNull()!!
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("refresh", sharedPreferences.getString("refreshToken", "").toString())
            .build()
        val request: okhttp3.Request = okhttp3.Request.Builder()
            .url("https://mental-diaries.herokuapp.com/api/users/login/refresh/")
            .method("POST", body)
            .build()
        GlobalScope.launch {
            val response = client.newCall(request).execute()
            val message = response.message
            if (message.toString() != "OK") {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Some error Occurred.", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                val responseObj = response.body?.string()
                val Jobject = JSONObject(responseObj)
                val accessToken = Jobject.get("access")
                editor.remove("accessToken").commit()
                editor.apply {
                    putString("accessToken", accessToken.toString())
                    apply()
                }
//                withContext(Dispatchers.Main){
//                    Toast.makeText(this@MainActivity,accessToken.toString(),Toast.LENGTH_LONG).show()
//                    Toast.makeText(this@MainActivity,sharedPreferences.getString("accessToken",""),Toast.LENGTH_LONG).show()
//                }
            }

        }
    }

    private fun insertToServer(sharedPreferences: SharedPreferences, entry: String, title: String) {
        val acccessToken = sharedPreferences.getString("accessToken", "").toString()

        val client = OkHttpClient().newBuilder()
            .build()
        val mediaType = "application/json".toMediaTypeOrNull()
        val body: RequestBody = RequestBody.create(
            mediaType,
            "{\r\n    \"username\": \"raunit\", \"\").toString()\",\r\n    \"entry\": \"$entry\",\r\n    \"entry_title\": \"$title\"\r\n}"
        )
        val request: Request = Request.Builder()
            .url("https://mental-diaries.herokuapp.com/api/diary/entry/")
            .method("POST", body)
            .addHeader(
                "Authorization",
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjQ1OTE4ODU2LCJpYXQiOjE2NDU5MTcwNTYsImp0aSI6Ijc2ZTZjNDk4YjY0MDRjNjQ5ODk3ZmY5ZjllOTY1NzQ5IiwidXNlcl9pZCI6NH0.Ix9fFseTq-Dd_aMdG93TYT8o1C9uc1LJdURtuiUR-nQ"
            )
            .addHeader("Content-Type", "application/json")
            .build()
        GlobalScope.launch {
           val str= APi().send(this@MainActivity,"","","raunit")

            val response = client.newCall(request).execute().message
//            val responseObj = response.body?.string()
//            val Jobject = JSONObject(responseObj)
//            val status = Jobject.get("Status")
//            if ("status.toString()"!= "Success") {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Some error Occurred on saving diary entry to server.",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            } else {
//
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "Successfully Saved."+str.toString()+response.toString(),
                        Toast.LENGTH_LONG
                    ).show()

                }
//            }
        }
    }
}