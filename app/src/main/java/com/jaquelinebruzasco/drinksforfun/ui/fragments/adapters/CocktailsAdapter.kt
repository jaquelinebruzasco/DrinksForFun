package com.jaquelinebruzasco.drinksforfun.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaquelinebruzasco.drinksforfun.R
import com.jaquelinebruzasco.drinksforfun.databinding.ItemListBinding
import com.jaquelinebruzasco.drinksforfun.domain.remote.model.DrinkModel
import com.jaquelinebruzasco.drinksforfun.ui.loadImage

class CocktailsAdapter(
    val action: (DrinkModel) -> Unit
) : RecyclerView.Adapter<CocktailsAdapter.CocktailsViewHolder>() {

    var list: MutableList<DrinkModel?> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CocktailsViewHolder(
        ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun onBindViewHolder(holder: CocktailsViewHolder, position: Int) {
        list[position]?.let { holder.bind(it) }
    }

    override fun getItemCount() = list.size

    inner class CocktailsViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DrinkModel) {
            binding.apply {
                tvName.text = data.name
                loadImage(
                    imageView = ivDrink,
                    code = data.image ?: ""
                )
            }
            binding.root.setOnClickListener { action(data) }
        }

    }
}

