package com.jaquelinebruzasco.drinksforfun.domain.remote.api

import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkResponseModel
import retrofit2.Response
import java.net.SocketException
import javax.inject.Inject

class DrinksForFunRepository @Inject constructor(
    private val api: DrinksForFunApi
) {
    suspend fun getCocktailByName(cocktailName: String): Response<DrinkResponseModel> {
        return api.getCocktailByName(cocktailName)
    }

    suspend fun getCocktailByFirstLetter(cocktailFirstLetter: String): Response<DrinkResponseModel> {
        return api.getCocktailByFirstLetter(cocktailFirstLetter)
    }

    suspend fun getRandomCocktail(): Response<DrinkResponseModel> {
        return api.getRandomCocktail()
    }
}