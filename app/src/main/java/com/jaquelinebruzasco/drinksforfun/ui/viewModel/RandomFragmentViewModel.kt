package com.jaquelinebruzasco.drinksforfun.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaquelinebruzasco.drinksforfun.domain.local.DrinksForFunLocalRepository
import com.jaquelinebruzasco.drinksforfun.domain.remote.api.DrinksForFunRepository
import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkModel
import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkResponseModel
import com.jaquelinebruzasco.drinksforfun.ui.fragments.adapters.IngredientsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomFragmentViewModel @Inject constructor(
    private val repository: DrinksForFunRepository,
    private val localRepository: DrinksForFunLocalRepository
): ViewModel() {

    private val _randomCocktail = MutableStateFlow<DrinksForFunRandomState>(DrinksForFunRandomState.Idle)
    val randomCocktail: StateFlow<DrinksForFunRandomState> = _randomCocktail

    fun getRandomCocktail() {
        viewModelScope.launch {
            _randomCocktail.value = DrinksForFunRandomState.Loading
            val response = repository.getRandomCocktail()
            if (response.isSuccessful) {
                response.body()?.let {
                    _randomCocktail.value = DrinksForFunRandomState.Success(it.drinks[0])
                } ?: kotlin.run { _randomCocktail.value = DrinksForFunRandomState.Failure("") }
            } else {
                _randomCocktail.value = DrinksForFunRandomState.Failure("")
            }
        }
    }

    fun setMeasuresAndIngredients(data: DrinkModel): MutableList<IngredientsModel> {
        return mutableListOf(
            IngredientsModel(data.measure1, data.ingredient1),
            IngredientsModel(data.measure2, data.ingredient2),
            IngredientsModel(data.measure3, data.ingredient3),
            IngredientsModel(data.measure4, data.ingredient4),
            IngredientsModel(data.measure5, data.ingredient5),
            IngredientsModel(data.measure6, data.ingredient6),
            IngredientsModel(data.measure7, data.ingredient7),
            IngredientsModel(data.measure8, data.ingredient8),
            IngredientsModel(data.measure9, data.ingredient9),
            IngredientsModel(data.measure10, data.ingredient10),
            IngredientsModel(data.measure11, data.ingredient11),
            IngredientsModel(data.measure12, data.ingredient12),
            IngredientsModel(data.measure13, data.ingredient13),
            IngredientsModel(data.measure14, data.ingredient14),
            IngredientsModel(data.measure15, data.ingredient15)
        )
    }

    fun isFavorite(id: Int): Boolean {
        var isFavorite = false
        viewModelScope.launch {
            isFavorite = localRepository.isFavorite(id)
        }
        return isFavorite
    }


    fun insert(drinkModel: DrinkModel) = viewModelScope.launch {
        localRepository.insert(drinkModel)
    }

    fun delete(drinkModel: DrinkModel) = viewModelScope.launch {
        localRepository.delete(drinkModel)
    }

}

sealed class DrinksForFunRandomState {
    object Idle: DrinksForFunRandomState()
    object Loading: DrinksForFunRandomState()
    class Success(val data: DrinkModel?): DrinksForFunRandomState()
    class Failure(val message: String): DrinksForFunRandomState()
}