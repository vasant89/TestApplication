package com.test.testapplication.main.photos

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import com.test.testapplication.R
import com.test.testapplication.SingleLiveEvent
import com.test.testapplication.data.source.Repository
import com.test.testapplication.main.searchplace.SearchPlaceViewModel
import com.test.testapplication.model.Result
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PhotoViewModel(
    context: Application,
    private val repository: Repository,
    val picasso: Picasso
) : AndroidViewModel(context) {

    @SuppressLint("StaticFieldLeak")
    private val context: Context = context.applicationContext //Application Context to avoid leaks.

    val isDataLoading = SingleLiveEvent<Boolean>()
    val isDataLoadingError = ObservableBoolean(false)

    val isLoading = ObservableBoolean(false)

    val empty = ObservableBoolean(false)

    val showToastMessage = SingleLiveEvent<String>()

    private fun showToastMassage(message: String) {
        showToastMessage.value = message
    }

    val showPhotosEvent = SingleLiveEvent<List<String>>()

    fun start() {
        getPhotos()
    }


    private fun getPhotos() {
        this.repository.getImages()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { isDataLoading.value = true }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                isDataLoadingError.set(true)
                isDataLoading.value = false
            }
            .doOnSuccess {
                isDataLoadingError.set(false)
                isDataLoading.value = false
            }.subscribe({
               showPhotosEvent.value = it.pugs
            }, {
                Log.e(TAG, it.message)
            })
    }

    companion object {
        private val TAG = PhotoViewModel::class.java.simpleName
    }
}