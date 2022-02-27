package com.shyptsolution.hackathon

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.shyptsolution.classproject.DataBase.DiaryEntity
import com.shyptsolution.classproject.DataBase.DiaryViewModel
import com.shyptsolution.hackathon.RecyclerView.RecyclerViewAllDiary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject


class AllDiary : AppCompatActivity(), RecyclerViewAllDiary.onItemClick {
    lateinit var adapter:RecyclerViewAllDiary
    lateinit var viewModel:DiaryViewModel
    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_diary)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.gradient))
        sharedPref = getSharedPreferences("diary", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        val recycerView=findViewById<RecyclerView>(R.id.recyclerViewAllDiary)
        recycerView.setHasFixedSize(false )
        recycerView.layoutManager = LinearLayoutManager(this)
        adapter= RecyclerViewAllDiary(this,this)
        recycerView.adapter=adapter

    getall()

        //ViewModel and DataBase
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[DiaryViewModel::class.java]

        viewModel.allDiaryEntry.observe(this){list->
            list?.let {
//                Toast.makeText(this,list.size.toString(),Toast.LENGTH_LONG).show()
                adapter.submitList(list)
            }
        }
    }

    override fun onDelete(diary: DiaryEntity) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are You Sure to delete this?")
        builder.setMessage("This will be deleted from the local storage only.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Delete This Post") { dialogInterface, which ->
            GlobalScope.launch {
                viewModel.repository.updateBookmark(true,diary.id)
            }
        }
        builder.setNegativeButton("Cancel") { dialogInterface, which ->
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

    }


    fun getall(){
        val acc=sharedPref.getString("accessToken","").toString()
        val username=sharedPref.getString("username","").toString()
        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .url("https://mental-diaries.herokuapp.com/api/diary/entry/?username=$username")
            .method("GET", null)
            .addHeader(
                "Authorization",
                "Bearer $acc"
            )
            .build()


        GlobalScope.launch(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            val obj=response.body?.string()
//            val jsonObject=JSONObject(obj)
            val jsonArray=JSONArray(obj)
//            viewModel.deleteDatabase()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.optString("entry_id").toString()
                val title = jsonObject.optString("entry_title").toString()
                val entry = jsonObject.optString("entry").toString()
                val date = jsonObject.optString("entry_date_time").toString()
                val status=(20..90).random()
                var diary=DiaryEntity(id,title,date,entry, "$status%",false)
               viewModel.insertDiary(diary)
            }


        }
    }


}