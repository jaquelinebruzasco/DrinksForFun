package com.jaquelinebruzasco.drinksforfun.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaquelinebruzasco.drinksforfun.R
import com.jaquelinebruzasco.drinksforfun.databinding.ActivityMainBinding
import com.jaquelinebruzasco.drinksforfun.databinding.FragmentDetailsBinding
import com.jaquelinebruzasco.drinksforfun.databinding.ToolbarBinding
import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkModel
import com.jaquelinebruzasco.drinksforfun.ui.fragments.adapters.IngredientsAdapter
import com.jaquelinebruzasco.drinksforfun.ui.loadImage
import com.jaquelinebruzasco.drinksforfun.ui.viewModel.DetailsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val viewModel by viewModels<DetailsFragmentViewModel>()
    private lateinit var _binding: FragmentDetailsBinding

    private val ingredientsAdapter by lazy {
        IngredientsAdapter(
            viewModel.setMeasuresAndIngredients(
                args.cocktailInformation
            )
        )
    }
    private val args: DetailsFragmentArgs by navArgs()

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
        loadInfo(args.cocktailInformation)
        setupRecycleView()
    }

    private fun setupRecycleView() = with(_binding) {
        rvDrinkIngredients.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ingredientsAdapter
        }
    }

    private fun loadInfo(data: DrinkModel) {
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

        }
    }
}