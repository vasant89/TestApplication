package com.test.testapplication.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.test.testapplication.databinding.RowPhotoBinding

class PhotoRvAdapter(val imageLoader: Picasso) : RecyclerView.Adapter<PhotoRvAdapter.ViewHolder>() {

    private val list: ArrayList<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val binding = RowPhotoBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    fun replaceList(list: List<String>) {
        this.list.clear()
        this.list.addAll(list)
        this.notifyDataSetChanged()
    }

    fun addList(list: List<String>) {
        this.list.addAll(list)
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position], position)


    inner class ViewHolder(val binding: RowPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, index: Int) {
            binding.apply {
                this.photoUrl = item
                this.picasso = imageLoader
                this.executePendingBindings()
            }
        }
    }
}