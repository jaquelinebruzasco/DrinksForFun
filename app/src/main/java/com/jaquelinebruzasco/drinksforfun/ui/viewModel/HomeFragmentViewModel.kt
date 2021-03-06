package com.jaquelinebruzasco.drinksforfun.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaquelinebruzasco.drinksforfun.domain.remote.api.DrinksForFunRepository
import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkModel
import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val repository: DrinksForFunRepository
) : ViewModel() {

    private val _cocktail = MutableStateFlow<DrinksForFunState>(DrinksForFunState.Idle)
    val cocktail: StateFlow<DrinksForFunState> = _cocktail

    fun getCocktailByName(cocktailName: String) {
        viewModelScope.launch {
            _cocktail.value = DrinksForFunState.Loading
            val response = repository.getCocktailByName(cocktailName)
            if (response.isSuccessful) {
                response.body()?.let {
                    mapIdForLocalDatabase(it.drinks)
                    _cocktail.value = DrinksForFunState.Success(it)
                } ?: kotlin.run { _cocktail.value = DrinksForFunState.Failure("") }
            } else {
                _cocktail.value = DrinksForFunState.Failure("")
            }
        }
    }

    fun getCocktailByLetter(letter: String) {
        viewModelScope.launch {
            _cocktail.value = DrinksForFunState.Loading
            val response = repository.getCocktailByFirstLetter(letter)
            if (response.isSuccessful) {
                response.body()?.let {
                    mapIdForLocalDatabase(it.drinks)
                    _cocktail.value = DrinksForFunState.Success(it)
                } ?: kotlin.run { _cocktail.value = DrinksForFunState.Failure("") }
            } else {
                _cocktail.value = DrinksForFunState.Failure("")
            }
        }
    }

    private fun mapIdForLocalDatabase(drinks: List<DrinkModel?>) {
        if (drinks != null) {
            drinks.forEach { item ->
                item?.id = item?.idApi?.toInt() ?: 0
            }
        }
    }
}

sealed class DrinksForFunState {
    object Idle: DrinksForFunState()
    object Loading: DrinksForFunState()
    class Success(val data: DrinkResponseModel?): DrinksForFunState()
    class Failure(val message: String): DrinksForFunState()
}