package com.example.converterapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_data")
data class CurrencyEntity(
    @PrimaryKey
    val currencyCode: String,
    val currencyName: String,
    val currencyRate: Double,
    val flagId: Int
)