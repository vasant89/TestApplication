package com.test.testapplication.binding

import android.databinding.BindingAdapter
import android.widget.RatingBar

object RatingBinding {

    private val TAG = RatingBinding::class.java.simpleName

    @BindingAdapter("rating")
    @JvmStatic
    fun setRating(ratingBar: RatingBar, rating: Double?) {
        rating?.let {
            ratingBar.rating = it.toFloat()
        }
    }
}