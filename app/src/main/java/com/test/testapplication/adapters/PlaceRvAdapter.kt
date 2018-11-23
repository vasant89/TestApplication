package com.test.testapplication.adapters

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.test.testapplication.databinding.RowPlaceBinding
import com.test.testapplication.model.Result
import com.test.testapplication.databinding.LoadingViewBinding

class PlaceRvAdapter(val imageLoader: Picasso) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: ArrayList<Result> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return when (p1) {
            TYPE_PLACE -> {
                ViewHolder(
                    RowPlaceBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )

            }
            TYPE_LOADING -> {
                ViewHolderLoading(
                    LoadingViewBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            else -> {
                throw IllegalAccessException("Unknown ViewType")
            }
        }
    }

    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, p1: Int) {
        val item = list[p1]

        when (getItemViewType(p1)) {
            TYPE_PLACE -> {
                val viewHolder = holder as ViewHolder
                viewHolder.bind(item, p1)
            }
            TYPE_LOADING -> {
                val viewHolder = holder as ViewHolderLoading
                viewHolder.bind()
            }
        }
    }


    inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Result, index: Int) {
            (binding as RowPlaceBinding).apply {
                this.result = item
                this.picasso = imageLoader
                this.executePendingBindings()
            }
        }
    }

    private var isLoadingAdded = false

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_PLACE
        } else {
            if (position == list.size - 1 && isLoadingAdded) TYPE_LOADING else TYPE_PLACE
        }
    }


    fun replaceList(list: List<Result>) {
        this.list.clear()
        this.list.addAll(list)
        this.notifyDataSetChanged()
    }

    fun addList(list: List<Result>) {
        if(isLoadingAdded){
            removeFooter()
        }
        this.list.addAll(list)
        this.notifyDataSetChanged()
    }

    fun addFooter() {
        this.list.add(Result())
        this.isLoadingAdded = true
        this.notifyDataSetChanged()
    }

    fun removeFooter() {
        this.list.removeAt(list.size - 1)
        this.isLoadingAdded = false
        this.notifyDataSetChanged()
    }

    inner class ViewHolderLoading(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            (binding as LoadingViewBinding).apply {
                executePendingBindings()
            }
        }
    }


    companion object {
        private val TAG = PlaceRvAdapter::class.java.simpleName

        private val TYPE_LOADING = -1
        private val TYPE_PLACE = 1
    }
}