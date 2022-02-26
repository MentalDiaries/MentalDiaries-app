package com.shyptsolution.classproject.DataBase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiaryViewModel(application: Application):AndroidViewModel(application) {
    var repository:DiaryRepository
    var allDiaryEntry:LiveData<List<DiaryEntity>>

    init {
        val dao=diaryDatabase(application.applicationContext).getDao()
        repository= DiaryRepository(dao)
        allDiaryEntry=repository.allDiaryEntry
    }
    fun insertDiary(diary:DiaryEntity)= viewModelScope.launch(Dispatchers.IO) {
        repository.insert(diary)
    }

    fun deleteDiary(id:String) =viewModelScope.launch(Dispatchers.IO){
        repository.deleteDiary(id)
    }
}