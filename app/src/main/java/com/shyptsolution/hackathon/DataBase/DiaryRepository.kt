package com.shyptsolution.classproject.DataBase

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class DiaryRepository(private val diaryDao: diaryDao) {
    val allDiaryEntry:LiveData<List<DiaryEntity>> =diaryDao.getAllDiary()

    suspend fun insert(diary:DiaryEntity){
        diaryDao.insert(diary)
    }

   suspend fun deleteDiary(id:String){
        diaryDao.deleteDiary(id)
    }
}