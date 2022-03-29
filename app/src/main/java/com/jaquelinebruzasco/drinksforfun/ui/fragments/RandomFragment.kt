package com.jaquelinebruzasco.drinksforfun.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jaquelinebruzasco.drinksforfun.R
import com.jaquelinebruzasco.drinksforfun.databinding.FragmentDetailsBinding
import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkModel
import com.jaquelinebruzasco.drinksforfun.ui.fragments.adapters.IngredientsAdapter
import com.jaquelinebruzasco.drinksforfun.ui.loadImage
import com.jaquelinebruzasco.drinksforfun.ui.viewModel.DrinksForFunRandomState
import com.jaquelinebruzasco.drinksforfun.ui.viewModel.RandomFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RandomFragment : Fragment() {

    private val viewModel by viewModels<RandomFragmentViewModel>()
    private lateinit var _binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservables()
        viewModel.getRandomCocktail()

    }

    private fun initObservables() {
        lifecycleScope.launchWhenCreated {
            viewModel.randomCocktail.collectLatest { state ->
                when (state) {
                    is DrinksForFunRandomState.Idle -> hideProgressBar()
                    is DrinksForFunRandomState.Loading -> showProgressBar()
                    is DrinksForFunRandomState.Failure -> {
                        hideProgressBar()
                        showFailureMessage(state.message)
                    }
                    is DrinksForFunRandomState.Success -> {
                        hideProgressBar()
                        state.data?.let { loadRandomView(it) }
                        viewModel.isFavorite.collectLatest {
                            _binding.ivFavorite.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    if (it) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                                )
                            )
                        }
                    }
                }
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
            nestedScroll.visibility = View.GONE
        }
    }

    private fun hideProgressBar() {
        _binding.apply {
            progressCircular.visibility = View.GONE
            nestedScroll.visibility = View.VISIBLE
        }

    }

    private fun loadRandomView(data: DrinkModel) {
        setupRecycleView(data)
        _binding.apply {
            loadImage(
                imageView = ivDrink,
                code = data.image ?: ""
            )
            tvDrinkName.text = data.name
            tvCategoryInfo.text = data.category
            tvAlcoholInfo.text = data.alcohol
            tvGlassInfo.text = data.glass
            if (data.instructions == null) {
                tvInstructionsInfo.text = R.string.no_instruction.toString()
            } else {
                tvInstructionsInfo.text = data.instructions
            }

            viewModel.getFavorite(data.id)

            ivFavorite.setOnClickListener {
                if (viewModel.isFavorite.value) {
                    viewModel.delete(data)
                    viewModel.getFavorite(data.id)
                } else {
                    viewModel.insert(data)
                    viewModel.getFavorite(data.id)
                }
            }

        }

    }

    private fun setupRecycleView(data: DrinkModel) = with(_binding) {
        rvDrinkIngredients.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = IngredientsAdapter(viewModel.setMeasuresAndIngredients(data))
        }
    }


}