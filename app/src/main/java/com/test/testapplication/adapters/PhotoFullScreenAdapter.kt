package com.test.testapplication.adapters

import android.support.constraint.ConstraintLayout
import android.support.v4.view.ViewPager
import android.view.ViewGroup
import android.view.LayoutInflater
import android.support.v4.view.PagerAdapter
import android.view.View
import com.squareup.picasso.Picasso
import com.test.testapplication.customviews.TouchImageView
import com.test.testapplication.databinding.RowPhotoFullScreenBinding


class PhotoFullScreenAdapter(
    private val imageLoader: Picasso,
    private val photos: ArrayList<String>,
    private val action: (string: String) -> Unit
) : PagerAdapter() {

    override fun getCount(): Int {
        return this.photos.size
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view === any as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = RowPhotoFullScreenBinding.inflate(
            LayoutInflater.from(container.context),
            container,
            false
        ).apply {
            this.photoUrl = photos[position]
            this.picasso = imageLoader
            this.ivPhoto.setOnLongClickListener {
                action(photos[position])
                true
            }
        }

        (container as ViewPager).addView(binding.root)

        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        (container as ViewPager).removeView(any as ConstraintLayout)
    }
}