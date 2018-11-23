package com.test.testapplication.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.test.testapplication.data.Contact
import com.test.testapplication.databinding.RowAddEmergencyContactBinding

class FavouritesAdapter(val imageLoader: Picasso,
                        val action: (contact: Contact, index: Int) -> Unit,
                        val isEmpty: (isEmpty: Boolean) -> Unit
) : RecyclerView.Adapter<FavouritesAdapter.AddEmergencyContactViewHolder>() {

    val list = ArrayList<Contact>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AddEmergencyContactViewHolder {
        val binding = RowAddEmergencyContactBinding.inflate(LayoutInflater.from(p0.context), p0, false)
        return AddEmergencyContactViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AddEmergencyContactViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    fun replaceList(list: ArrayList<Contact>) {
        this.list.clear()
        this.list.addAll(list)
        isEmpty(this.list.isEmpty())
        this.notifyDataSetChanged()
    }

    fun addList(list: ArrayList<Contact>) {
        this.list.addAll(list)
        isEmpty(this.list.isEmpty())
        this.notifyDataSetChanged()
    }


    inner class AddEmergencyContactViewHolder(val binding: RowAddEmergencyContactBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Contact, index: Int) {
            binding.apply {
                this.picasso = imageLoader
                this.contact = item
                this.ivAddRemove.setOnClickListener {
                    action(item, index)
                }
                executePendingBindings()
            }
        }
    }
}