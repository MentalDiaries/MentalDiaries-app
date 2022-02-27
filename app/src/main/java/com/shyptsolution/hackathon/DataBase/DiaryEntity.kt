package com.shyptsolution.classproject.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter


@Entity(tableName="diary_info")
data class DiaryEntity(
    @PrimaryKey
    val id:String,
    val title:String,
    val date:String,
    val diary:String,
    val status:String,
    var delete:Boolean
)


