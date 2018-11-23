package com.test.testapplication.adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.test.testapplication.R
import com.test.testapplication.data.Contact
import com.test.testapplication.databinding.RowAddEmergencyContactBinding
import com.test.testapplication.databinding.RowContactBinding

class AddEmergencyContactRvAdapter(
    val imageLoader: Picasso,
    val action: (contact: Contact, index: Int) -> Unit,
    val isEmpty: (isEmpty: Boolean) -> Unit
) : RecyclerView.Adapter<AddEmergencyContactRvAdapter.AddEmergencyContactViewHolder>(), Filterable {

    var query = ""

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                query = charSequence.toString()
                val filterResults = FilterResults()
                filterResults.values = if (query.isEmpty()) {
                    list
                } else {
                    list.filter { it.firstName.toLowerCase().contains(query.toLowerCase()) } as ArrayList<Contact>
                }
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

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AddEmergencyContactViewHolder {
        val binding = RowAddEmergencyContactBinding.inflate(LayoutInflater.from(p0.context), p0, false)
        return AddEmergencyContactViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return contactListFiltered.size
    }

    override fun onBindViewHolder(holder: AddEmergencyContactViewHolder, position: Int) {
        holder.bind(contactListFiltered[position], position)
    }

    fun replaceList(list: ArrayList<Contact>) {
        this.list.clear()
        this.list.addAll(list)
        this.contactListFiltered = getFilteredList(list)
        isEmpty(this.contactListFiltered.isEmpty())
        this.notifyDataSetChanged()
    }

    fun addList(list: ArrayList<Contact>) {
        this.list.addAll(list)
        this.contactListFiltered.addAll(getFilteredList(list))
        isEmpty(this.contactListFiltered.isEmpty())
        this.notifyDataSetChanged()
    }


    private fun getFilteredList(listToFilter: ArrayList<Contact>):ArrayList<Contact>{
        return   if(query.isEmpty()){
            listToFilter
        }else{
            listToFilter.filter { it.firstName.toLowerCase().contains(query.toLowerCase()) } as ArrayList<Contact>
        }
    }

    inner class AddEmergencyContactViewHolder(val binding: RowAddEmergencyContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
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