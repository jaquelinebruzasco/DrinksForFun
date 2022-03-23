package com.jaquelinebruzasco.drinksforfun.domain.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkModel

@Database(entities = [DrinkModel::class], version = 1, exportSchema = false)
abstract class DrinksDatabase : RoomDatabase() {
    abstract fun drinksDao(): DrinksDao
}
