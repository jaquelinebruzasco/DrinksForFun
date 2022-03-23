package com.jaquelinebruzasco.drinksforfun.domain.local

import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkModel
import javax.inject.Inject

class DrinksForFunLocalRepository @Inject constructor(
    private val dao: DrinksDao
) {
    suspend fun insert(drinkModel: DrinkModel) = dao.insert(drinkModel)
    fun getAll() = dao.getAll()
    suspend fun delete(drinkModel: DrinkModel) = dao.delete(drinkModel)
}