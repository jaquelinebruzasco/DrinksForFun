package com.jaquelinebruzasco.drinksforfun.ui

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jaquelinebruzasco.drinksforfun.R

fun loadImage(
    imageView: ImageView,
    code: String
) {
    Glide.with(imageView.context)
        .load(code)
        .placeholder(R.drawable.ic_drink)
        .into(imageView)

}