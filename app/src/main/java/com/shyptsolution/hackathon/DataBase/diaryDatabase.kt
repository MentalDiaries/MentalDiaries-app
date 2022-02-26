package com.shyptsolution.classproject.DataBase

import android.content.Context
import androidx.room.*

@Database(
    entities = [DiaryEntity::class],
    version = 3
)
abstract class diaryDatabase:RoomDatabase() {
    abstract fun getDao():diaryDao

    companion object {
        @Volatile
        private var instance: diaryDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance =it
            }

        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            diaryDatabase::class.java,
            "diary_info"
        ).build()
    }

}