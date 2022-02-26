package com.shyptsolution.hackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shyptsolution.classproject.DataBase.DiaryEntity
import com.shyptsolution.classproject.DataBase.DiaryViewModel
import com.shyptsolution.hackathon.RecyclerView.RecyclerViewAllDiary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllDiary : AppCompatActivity(), RecyclerViewAllDiary.onItemClick {
    lateinit var adapter:RecyclerViewAllDiary
    lateinit var viewModel:DiaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_diary)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val recycerView=findViewById<RecyclerView>(R.id.recyclerViewAllDiary)
        recycerView.setHasFixedSize(false )
        recycerView.layoutManager = LinearLayoutManager(this)
        adapter= RecyclerViewAllDiary(this,this)
        recycerView.adapter=adapter



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
                viewModel.repository.deleteDiary(diary.id)
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


}