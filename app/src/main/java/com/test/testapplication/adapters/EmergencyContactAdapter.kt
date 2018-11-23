package com.test.testapplication.adapters

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.test.testapplication.data.Contact
import com.test.testapplication.databinding.*

class EmergencyContactAdapter(
    val imageLoader: Picasso,
    val action: (isAddContact: Boolean, position: Int, item: Contact) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: ArrayList<Contact> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return when (p1) {
            TYPE_CONTACT -> {
                ContactViewHolder(
                    RowEmergencyContactBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )

            }
            TYPE_ADD_CONTACT -> {
                AddContactViewHolder(
                    RowAddContactViewBinding
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
            TYPE_CONTACT -> {
                val viewHolder = holder as ContactViewHolder
                viewHolder.bind(item, p1)
            }
            TYPE_ADD_CONTACT -> {
                val viewHolder = holder as AddContactViewHolder
                viewHolder.bind(item, p1)
            }
        }
    }


    inner class ContactViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Contact, index: Int) {
            (binding as RowEmergencyContactBinding).apply {
                this.contact = item
                this.picasso = imageLoader
                this.root.setOnClickListener {
                    action(false, index, item)
                }
                this.executePendingBindings()
            }
        }
    }

    inner class AddContactViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Contact, index: Int) {
            (binding as RowAddContactViewBinding).apply {
                this.root.setOnClickListener {
                    action(true, index, item)
                }
                this.executePendingBindings()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == list.size - 1) {
            TYPE_ADD_CONTACT
        } else {
            TYPE_CONTACT
        }

    }


    fun replaceList(list: List<Contact>) {
        this.list.clear()
        this.list.addAll(list)
        this.notifyDataSetChanged()
    }

    fun addList(list: List<Contact>) {
        this.list.addAll(list)
        this.notifyDataSetChanged()
    }


    companion object {
        private val TAG = PlaceRvAdapter::class.java.simpleName

        private val TYPE_ADD_CONTACT = -1
        private val TYPE_CONTACT = 1
    }
}