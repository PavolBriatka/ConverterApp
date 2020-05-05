package com.example.converterapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CurrencyEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao

    companion object {
        var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "app_database"

        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()
                }
            }
            return INSTANCE
        }

        fun  destroyDatabase(){
            INSTANCE = null
        }
    }
}