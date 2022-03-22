package com.jaquelinebruzasco.drinksforfun.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaquelinebruzasco.drinksforfun.databinding.ItemIngredientsListBinding

class IngredientsAdapter(
    private val list: MutableList<IngredientsModel>
) : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = IngredientsViewHolder(
        ItemIngredientsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: IngredientsAdapter.IngredientsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class IngredientsViewHolder(private val binding: ItemIngredientsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: IngredientsModel) {
            binding.tvItemMeasure.text = data.measure ?: ""
            binding.tvItemIngredient.text = data.ingredient ?: ""
            if (binding.tvItemMeasure.text.isEmpty()) binding.tvItemMeasure.visibility = View.GONE else View.VISIBLE
            if (binding.tvItemIngredient.text.isEmpty()) binding.tvItemIngredient.visibility = View.GONE else View.VISIBLE
        }
    }

}