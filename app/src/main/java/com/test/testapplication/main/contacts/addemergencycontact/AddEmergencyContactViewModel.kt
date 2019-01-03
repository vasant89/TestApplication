package com.test.testapplication.main.contacts.addemergencycontact

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField

import com.squareup.picasso.Picasso
import com.test.testapplication.SingleLiveEvent

import com.test.testapplication.data.Contact
import com.test.testapplication.data.source.Repository
import com.test.testapplication.main.photos.PhotoViewModel


class AddEmergencyContactViewModel(
    context: Application,
    val repository: Repository,
    val picasso: Picasso
) : AndroidViewModel(context) {

    @SuppressLint("StaticFieldLeak")
    private val context: Context = context.applicationContext //Application Context to avoid leaks.

    val isDataLoading = SingleLiveEvent<Boolean>()
    val isDataLoadingError = ObservableBoolean(false)

    val isLoading = ObservableBoolean(false)

    val isEmpty = ObservableBoolean(false)

    val showToastMessage = SingleLiveEvent<String>()

    private fun showToastMassage(message: String) {
        showToastMessage.value = message
    }

    var contactList = ObservableArrayList<Contact>()

    fun start() {

    }

    val query = ObservableField<String>("")

    fun onSearch(text: String) {
        query.set(text)
    }

    val isMax = ObservableBoolean(false)

    fun addRemoveEmergencyContact(contact: Contact, index: Int) {
        val count = contactList.filter { it.isEmergencyContact }.count()
        contact.isEmergencyContact = !contact.isEmergencyContact
        if(contact.isEmergencyContact){
            if(count < 10){
                this.repository.updateContact(contact)
                this.isMax.set(false)
            }else{
                this.isMax.set(true)
            }
        }else{
            this.isMax.set(false)
            this.repository.updateContact(contact)
        }

    }


    companion object {
        private val TAG = PhotoViewModel::class.java.simpleName
    }
}