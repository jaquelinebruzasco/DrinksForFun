package com.jaquelinebruzasco.drinksforfun.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaquelinebruzasco.drinksforfun.domain.local.DrinksForFunLocalRepository
import com.jaquelinebruzasco.drinksforfun.domain.remote.api.DrinksForFunRepository
import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkModel
import com.jaquelinebruzasco.drinksforfun.ui.fragments.adapters.IngredientsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(
    private val repository: DrinksForFunRepository,
    private val localRepository: DrinksForFunLocalRepository
) : ViewModel() {

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

    fun insert(drinkModel: DrinkModel) = viewModelScope.launch {
        localRepository.insert(drinkModel)
    }
}