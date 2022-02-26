package com.shyptsolution.hackathon

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationView
import com.shyptsolution.classproject.DataBase.DiaryEntity
import com.shyptsolution.classproject.DataBase.DiaryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var viewModel:DiaryViewModel
    lateinit var title:EditText
    lateinit var diaryNote:EditText
    lateinit var editor:SharedPreferences.Editor
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //SharedPreference
        val sharedPref=getSharedPreferences("diary", Context.MODE_PRIVATE)
        editor=sharedPref.edit()
        setContentView(R.layout.activity_main)

        //Title and Diary Note
         title=findViewById<EditText>(R.id.diaryTitle)
        diaryNote=findViewById<EditText>(R.id.diaryNote)


        //Menu Options
        val navigationView = findViewById<NavigationView>(R.id.navmenu)
        val navheader = navigationView.getHeaderView(0)
        val driver:DrawerLayout=findViewById(R.id.drawerlayout)
//        supportActionBar?.hide()
        toggle= ActionBarDrawerToggle(this,driver,R.string.open,R.string.close)
        driver.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //On item click menu
       navigationView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener {
           item ->
           when (item.itemId) {
               R.id.analysis -> {
                   driver.closeDrawer(GravityCompat.START)

               }

               R.id.appointment -> {

               }

               R.id.totalDiary -> {
                   startActivity(Intent(this,AllDiary::class.java))
                   driver.closeDrawer(GravityCompat.START)

               }
               R.id.logout -> {
                   startActivity(Intent(this,Login::class.java))
                   driver.closeDrawer(GravityCompat.START)
               }

               R.id.exit ->{
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

        title.setText(sharedPref.getString("title","").toString())
        diaryNote.setText(sharedPref.getString("lastDiary","").toString())



    }

    @SuppressLint("SimpleDateFormat")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return when(item.itemId){
            R.id.saveDiary -> {
                lifecycleScope.launch {
                    val titleText=title.text.toString()
                    val todayDiary=diaryNote.text.toString()
                    val df = SimpleDateFormat("dd-MM-yyyy, hh:mm aa")
                    var localDate = df.format(Date())
                    val id=Calendar.getInstance().timeInMillis.toString()
                    val Diary=DiaryEntity(id,titleText,localDate,todayDiary,"Status")
                    viewModel.repository.insert(Diary)
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity,"Saved",Toast.LENGTH_LONG).show()
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
        menuInflater.inflate(R.menu.savemenu,menu)
        val saveItem=menu.findItem(R.id.saveDiary)

        if(saveItem!=null){
            saveItem.isVisible = false

        }
        if(title.text.toString().isNotBlank() && diaryNote.text.toString().isNotBlank()){
            saveItem.setVisible(true)
        }
        title.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                saveItem.isVisible = title.text.isNotBlank() || diaryNote.text.isNotBlank()
                editor.apply {
                    putString("title",title.text.toString())
                    apply()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                saveItem.isVisible = title.text.isNotBlank() || diaryNote.text.isNotBlank()
                editor.apply {
                    putString("title",title.text.toString())
                    apply()
                }
            }

        })
        diaryNote.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                saveItem.isVisible = title.text.isNotBlank() || diaryNote.text.isNotBlank()
                editor.apply {
                    putString("lastDiary",diaryNote.text.toString())
                    apply()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                saveItem.isVisible = title.text.isNotBlank() || diaryNote.text.isNotBlank()
                editor.apply {
                    putString("lastDiary",diaryNote.text.toString())
                    apply()
                }
            }

        })
        return super.onCreateOptionsMenu(menu)
    }


}