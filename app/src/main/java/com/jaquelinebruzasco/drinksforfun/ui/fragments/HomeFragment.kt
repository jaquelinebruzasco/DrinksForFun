package com.jaquelinebruzasco.drinksforfun.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jaquelinebruzasco.drinksforfun.R
import com.jaquelinebruzasco.drinksforfun.databinding.FragmentHomeBinding
import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkModel
import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkResponseModel
import com.jaquelinebruzasco.drinksforfun.ui.fragments.adapters.CocktailsAdapter
import com.jaquelinebruzasco.drinksforfun.ui.fragments.adapters.LettersAdapter
import com.jaquelinebruzasco.drinksforfun.ui.viewModel.DrinksForFunState.*
import com.jaquelinebruzasco.drinksforfun.ui.viewModel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeFragmentViewModel>()
    private lateinit var _binding: FragmentHomeBinding
    private val cocktailsAdapter by lazy { CocktailsAdapter(::navigateToDetails) }
    private val lettersAdapter by lazy { LettersAdapter(createAlphabeticalList(), ::showCocktailByFirstLetter) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleView()
        initObservables()
    }

    private fun initObservables() {
        lifecycleScope.launchWhenCreated {
            viewModel.cocktail.collectLatest { state ->
                when (state) {
                    is Idle -> hideProgressBar()
                    is Loading -> showProgressBar()
                    is Failure -> {
                        hideProgressBar()
                        showFailureMessage(state.message)
                    }
                    is Success -> {
                        hideProgressBar()
                        loadDrinkView(state.data)
                    }
                }

            }
        }
    }

    private fun setupRecycleView() = with(_binding) {
        rvLetterList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = lettersAdapter
        }
        rvDrinksList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cocktailsAdapter
        }
    }

    private fun createAlphabeticalList(): MutableList<Char> {
        var character = 'A'
        val list = mutableListOf<Char>()
        while (character <= 'Z') {
            list.add(character)
            ++character
        }
        return list
    }

    private fun loadDrinkView(data: DrinkResponseModel?) {
        _binding.apply {
            if (data?.drinks == null) {
                rvDrinksList.visibility = View.GONE
                tvError.visibility = View.VISIBLE
            } else {
                rvDrinksList.visibility = View.VISIBLE
                tvError.visibility = View.GONE
                cocktailsAdapter.list = data.drinks.toMutableList()
            }
        }
    }

    private fun showFailureMessage(message: String) {
        Snackbar.make(
            requireView(),
            resources.getString(R.string.error_message),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun showProgressBar() {
        _binding.apply {
            progressCircular.visibility = View.VISIBLE
            rvDrinksList.visibility = View.GONE
            rvLetterList.visibility = View.GONE
            svSearch.visibility = View.GONE
        }
    }

    private fun hideProgressBar() {
        _binding.apply {
            progressCircular.visibility = View.GONE
            rvDrinksList.visibility = View.VISIBLE
            rvLetterList.visibility = View.VISIBLE
            svSearch.visibility = View.VISIBLE
        }
    }

    private fun showCocktailByFirstLetter(letter: String) {
        viewModel.getCocktailByLetter(letter)
    }

    private fun navigateToDetails(data: DrinkModel) {
        val navigation = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(data)
        findNavController().navigate(navigation)
    }
}