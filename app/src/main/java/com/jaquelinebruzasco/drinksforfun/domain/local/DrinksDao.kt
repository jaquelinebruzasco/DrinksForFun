package com.jaquelinebruzasco.drinksforfun.domain.local

import androidx.room.*
import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkModel
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(drinkModel: DrinkModel) : Long

    @Query("SELECT * FROM drinkmodel order by name")
    fun getAll(): Flow<List<DrinkModel>>

    @Query("SELECT EXISTS (SELECT * FROM drinkmodel WHERE id = :id)")
    suspend fun isFavorite(id: Int) : Boolean

    @Delete
    suspend fun delete(drinkModel: DrinkModel)
}