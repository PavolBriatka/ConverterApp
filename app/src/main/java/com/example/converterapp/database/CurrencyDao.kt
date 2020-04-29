package com.example.converterapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCurrency(currency: CurrencyEntity)

    @Query("SELECT * FROM currency_data")
    fun retrieveCurrencies(): List<CurrencyEntity>
}