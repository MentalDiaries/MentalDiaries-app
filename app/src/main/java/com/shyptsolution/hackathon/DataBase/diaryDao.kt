package com.shyptsolution.classproject.DataBase

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface diaryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(diary:DiaryEntity)

    @Query("SELECT * FROM diary_info ORDER BY id")
    fun getAllDiary():LiveData<List<DiaryEntity>>

    @Query("DELETE FROM diary_info WHERE id = :id")
    fun deleteDiary(id:String)


}