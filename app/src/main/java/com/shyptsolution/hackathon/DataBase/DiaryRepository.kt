package com.shyptsolution.classproject.DataBase

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DiaryRepository(private val diaryDao: diaryDao) {
    val allDiaryEntry:LiveData<List<DiaryEntity>> =diaryDao.getAllDiary()

    suspend fun insert(diary:DiaryEntity){
        diaryDao.insert(diary)
    }

   suspend fun deleteDiary(id:String){
        diaryDao.deleteDiary(id)
    }

    suspend fun deleteAll(){
        diaryDao.deleteDatabase()
    }


}