package com.jaquelinebruzasco.drinksforfun.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaquelinebruzasco.drinksforfun.databinding.ItemLetterListBinding

class LettersAdapter(
    private val list: MutableList<Char>,
    val action: (String) -> Unit
) : RecyclerView.Adapter<LettersAdapter.LettersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LettersViewHolder(
        ItemLetterListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: LettersViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class LettersViewHolder(private val binding: ItemLetterListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Char) {
            val charToString = data.toString()
            binding.tvLetter.text = charToString
            binding.root.setOnClickListener { action(charToString) }
        }
    }
}