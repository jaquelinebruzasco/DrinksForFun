package com.jaquelinebruzasco.drinksforfun.domain.remote.api

import com.jaquelinebruzasco.drinksforfun.domain.remote.model.ApiConstants
import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DrinksForFunApi {
    @GET("search.php")
    suspend fun getCocktailByName(
        @Query("s")
        cocktailName: String,
        @Query("api_key")
        apiKey: String = ApiConstants.API_KEY
    ): Response<DrinkResponseModel>

    @GET("search.php")
    suspend fun getCocktailByFirstLetter(
        @Query("f")
        cocktailFirstLetter: String
    ): Response<DrinkResponseModel>

    @GET("random.php")
    suspend fun getRandomCocktail(): Response<DrinkResponseModel>
}