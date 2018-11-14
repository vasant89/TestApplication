package com.test.testapplication.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.test.testapplication.databinding.RowPlaceBinding
import com.test.testapplication.model.Result

class PlaceRvAdapter(val imageLoader: Picasso) : RecyclerView.Adapter<PlaceRvAdapter.ViewHolder>() {

    private val list: ArrayList<Result> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val binding = RowPlaceBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    fun replaceList(list: List<Result>) {
        this.list.clear()
        this.list.addAll(list)
        this.notifyDataSetChanged()
    }

    fun addList(list: List<Result>) {
        this.list.addAll(list)
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position], position)


    inner class ViewHolder(val binding: RowPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Result, index: Int) {
            binding.apply {
                this.result = item
                this.picasso = imageLoader
                this.executePendingBindings()
            }
        }
    }
}