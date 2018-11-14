package com.test.testapplication.adapters

import java.util.ArrayList
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.view.LayoutInflater
import com.squareup.picasso.Picasso
import com.test.testapplication.R
import com.test.testapplication.databinding.RowPhotoBinding

class PhotoGrAdapter(
    private val imageLoader: Picasso,
    private val photos: ArrayList<String>,
    private val photoClickListener: PhotoClickListener
) : BaseAdapter() {

    override fun getCount(): Int {
        return this.photos.size
    }

    override fun getItem(position: Int): String {
        return this.photos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: RowPhotoBinding
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_photo, null)
            binding = RowPhotoBinding.bind(convertView)
            convertView.tag = binding
        } else {
            binding = convertView.tag as RowPhotoBinding
        }
        binding.apply {
            this.photoUrl = getItem(position)
            this.picasso = imageLoader
            this.root.setOnClickListener {

                photoClickListener.onPhotoClick(position, photos)
            }
            this.executePendingBindings()
        }
        return binding.root
    }


    interface PhotoClickListener {
        fun onPhotoClick(index: Int, photos: ArrayList<String>)
    }

    companion object {
        private val TAG = PhotoGrAdapter::class.java.simpleName
    }

}