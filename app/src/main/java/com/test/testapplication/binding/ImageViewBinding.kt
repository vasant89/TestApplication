package com.test.testapplication.binding

import android.databinding.BindingAdapter
import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.Picasso

object ImageViewBinding {

    private val TAG = ImageViewBinding::class.java.simpleName

    @BindingAdapter("imageUrl", "picasso")
    @JvmStatic
    fun showImage(imageView: ImageView, imageUrl: String?, picasso: Picasso) {
        Log.e(TAG, imageUrl.toString())
        if (!imageUrl.isNullOrEmpty()) {
            picasso.setIndicatorsEnabled(false)
            picasso.load(imageUrl)
                .resize(600, 200)
                .centerInside()
                .into(imageView)
        }
    }

}