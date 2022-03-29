package com.jaquelinebruzasco.drinksforfun.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jaquelinebruzasco.drinksforfun.R
import com.jaquelinebruzasco.drinksforfun.databinding.FragmentFavoriteBinding
import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkModel
import com.jaquelinebruzasco.drinksforfun.ui.fragments.adapters.CocktailsAdapter
import com.jaquelinebruzasco.drinksforfun.ui.viewModel.DrinksForFunLocalState
import com.jaquelinebruzasco.drinksforfun.ui.viewModel.FavoriteFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private val viewModel by viewModels<FavoriteFragmentViewModel>()
    private lateinit var _binding: FragmentFavoriteBinding
    private val cocktailsAdapter by lazy { CocktailsAdapter(::navigateToDetails) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleView()
        initObservables()
        viewModel.favoriteDrinks()
    }

    private fun initObservables() {
        lifecycleScope.launchWhenCreated {
            viewModel.favorites.collect { state ->
                when (state) {
                    is DrinksForFunLocalState.Empty -> {
                        _binding.tvEmptyList.visibility = View.VISIBLE
                    }
                    is DrinksForFunLocalState.Success -> {
                        _binding.tvEmptyList.visibility = View.GONE
                        cocktailsAdapter.list = state.data.toMutableList()
                    }
                }
            }
        }
    }

    private fun setupRecycleView() = with(_binding) {
        rvDrinksList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cocktailsAdapter
        }
        ItemTouchHelper(itemTouchHelperCallback()).attachToRecyclerView(rvDrinksList)
    }

    private fun navigateToDetails(data: DrinkModel) {
        val navigation = FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(data)
        findNavController().navigate(navigation)
    }

    private fun itemTouchHelperCallback(): ItemTouchHelper.SimpleCallback{
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val cocktail = cocktailsAdapter.getCocktailAtPosition(viewHolder.adapterPosition)
                cocktail?.let {
                    viewModel.delete(it).also {
                        Toast.makeText(requireContext(), R.string.delete_cocktail, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}