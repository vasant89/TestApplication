package com.test.testapplication.main.contacts

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import com.test.testapplication.adapters.AddEmergencyContactRvAdapter
import com.test.testapplication.adapters.AllContactRvAdapter
import com.test.testapplication.adapters.EmergencyContactAdapter
import com.test.testapplication.adapters.FavouritesAdapter
import com.test.testapplication.data.Contact

object ContactBinding {

    private val TAG = ContactBinding::class.java.simpleName

    @BindingAdapter("emergency")
    @JvmStatic
    fun setEmergencyList(recyclerView: RecyclerView, emergency: List<Contact>) {
        recyclerView.apply {
            (adapter as EmergencyContactAdapter).apply {
                val emergencyList = emergency.filter { it.isEmergencyContact } as ArrayList
                replaceList(emergencyList.apply {
                    add(Contact("", "", "Add", "Contact",""))
                })
            }
        }
    }

    @BindingAdapter("favourites")
    @JvmStatic
    fun setFavouritesList(recyclerView: RecyclerView, favourites: List<Contact>) {
        recyclerView.apply {
            (adapter as FavouritesAdapter).apply {
                val favList = ArrayList<Contact>()
                favourites.forEachIndexed { index, contact ->
                    if (index < 3) {
                        favList.add(contact)
                    } else {
                        return@forEachIndexed
                    }
                }
                replaceList(favList)
            }
        }
    }

    @BindingAdapter("allContacts")
    @JvmStatic
    fun setAllContacts(recyclerView: RecyclerView, allContact: ArrayList<Contact>) {
        recyclerView.apply {
            when (adapter) {
                is AllContactRvAdapter -> {
                    (adapter as AllContactRvAdapter).replaceList(allContact)
                }
                is AddEmergencyContactRvAdapter -> {
                    (adapter as AddEmergencyContactRvAdapter).replaceList(allContact)
                }
            }
        }
    }

    @BindingAdapter("filterContact")
    @JvmStatic
    fun showFilterContact(recyclerView: RecyclerView, query: String?) {
        recyclerView.apply {
            when (adapter) {
                is AllContactRvAdapter -> {
                    (adapter as AllContactRvAdapter).filter.filter(query)
                }
                is AddEmergencyContactRvAdapter -> {
                    (adapter as AddEmergencyContactRvAdapter).filter.filter(query)
                }
            }
        }
    }
}