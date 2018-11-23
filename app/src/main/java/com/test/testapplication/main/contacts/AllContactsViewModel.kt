package com.test.testapplication.main.contacts

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import com.squareup.picasso.Picasso
import com.test.testapplication.SingleLiveEvent
import com.test.testapplication.data.Contact
import com.test.testapplication.data.source.Repository
import com.test.testapplication.main.photos.PhotoViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AllContactsViewModel(
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


    fun getContacts() {
        this.repository.getContacts()
            .toFlowable()
            .flatMap {
                Flowable.fromIterable(it)
            }

            .subscribeOn(Schedulers.io())
            .doOnSubscribe { isDataLoading.value = true }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                isDataLoadingError.set(true)
                isDataLoading.value = false
            }
            .doOnComplete {
                isDataLoadingError.set(false)
                isDataLoading.value = false
            }.subscribe({
                this.repository.insert(it)
            }, {
                Log.e(TAG, it.message)
            })
    }

    val query = ObservableField<String>("")

    fun onSearch(text: String) {
        query.set(text)
    }

    companion object {
        private val TAG = PhotoViewModel::class.java.simpleName
    }
}