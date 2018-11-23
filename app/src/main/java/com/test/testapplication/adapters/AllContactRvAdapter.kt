package com.test.testapplication.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.squareup.picasso.Picasso
import com.test.testapplication.data.Contact
import com.test.testapplication.databinding.RowContactBinding


class AllContactRvAdapter(
    val imageLoader: Picasso,
    val action: (phone: String) -> Unit,
    val isEmpty: (isEmpty: Boolean) -> Unit
) : RecyclerView.Adapter<AllContactRvAdapter.ViewHolder>(), Filterable {
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                contactListFiltered = if (charString.isEmpty()) {
                    list
                } else {
                    val filteredList = arrayListOf<Contact>()
                    for (row in list) {
                        val firstName = row.firstName
                        if (firstName.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }

                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = contactListFiltered
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                contactListFiltered = p1?.let {
                    it.values as ArrayList<Contact>
                } ?: arrayListOf()
                isEmpty(contactListFiltered.isEmpty())
                notifyDataSetChanged()
            }

        }
    }

    var contactListFiltered = ArrayList<Contact>()
    val list = ArrayList<Contact>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val binding = RowContactBinding.inflate(LayoutInflater.from(p0.context), p0, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return contactListFiltered.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(contactListFiltered[p1], p1)
    }

    fun replaceList(list: ArrayList<Contact>) {
        this.list.clear()
        this.list.addAll(list)
        this.contactListFiltered = list
        isEmpty(this.contactListFiltered.isEmpty())
        this.notifyDataSetChanged()
    }

    fun addList(list: ArrayList<Contact>) {
        this.list.addAll(list)
        this.contactListFiltered.addAll(list)
        isEmpty(this.contactListFiltered.isEmpty())
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: RowContactBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Contact, index: Int) {
            binding.apply {
                this.picasso = imageLoader
                this.contact = item
                this.ivCall.setOnClickListener {
                    item.phone?.let { phone ->
                        action(phone)
                    }
                }
                executePendingBindings()
            }
        }
    }
}