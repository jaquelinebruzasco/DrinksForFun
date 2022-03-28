package com.jaquelinebruzasco.drinksforfun.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaquelinebruzasco.drinksforfun.domain.local.DrinksForFunLocalRepository
import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkModel
import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteFragmentViewModel @Inject constructor(
    private val localRepository: DrinksForFunLocalRepository
) : ViewModel() {

    private val _favorites = MutableStateFlow<DrinksForFunLocalState>(DrinksForFunLocalState.Empty)
    val favorites: StateFlow<DrinksForFunLocalState> = _favorites

    fun favoriteDrinks() = viewModelScope.launch {
        localRepository.getAll().collectLatest {
            if (it.isNullOrEmpty()) {
                _favorites.value = DrinksForFunLocalState.Empty
            } else {
                _favorites.value = DrinksForFunLocalState.Success(it)
            }
        }
    }
}

sealed class DrinksForFunLocalState {
    object Empty : DrinksForFunLocalState()
    class Success(val data: List<DrinkModel>) : DrinksForFunLocalState()
}