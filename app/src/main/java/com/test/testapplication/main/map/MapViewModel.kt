package com.test.testapplication.main.map

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import com.test.testapplication.R
import com.test.testapplication.SingleLiveEvent
import com.test.testapplication.data.source.Repository
import com.test.testapplication.model.Result
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MapViewModel(
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

    val showPlaceListEvent = SingleLiveEvent<Void>()

    fun showList() {
        showPlaceListEvent.call()
    }

    fun start() {

    }

    var nextPageToken: String? = null

    val place = ObservableField<String>()

    val replacePlacesEvent = SingleLiveEvent<List<Result>>()
    val addPlacesEvent = SingleLiveEvent<List<Result>>()

    fun getPlaceDetails(latLng: LatLng, radius: Double) {
        val location = "${latLng.latitude},${latLng.longitude}"
        this.repository.getPlaceDetail(
            location,
            radius,
            context.getString(R.string.google_maps_key)
        )
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
                nextPageToken = it.nextPageToken
                it.results?.let { results ->
                    replacePlacesEvent.value = results
                }
            }, {
                Log.e(TAG, it.message)
            })
    }

    fun getPlaceDetailNextPage() {
        this.repository.getPlaceDetailNextPage(nextPageToken!!, context.getString(R.string.google_maps_key))
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {isLoading.set(true)}
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                isDataLoadingError.set(true)
                isLoading.set(false)
            }
            .doOnSuccess {
                isDataLoadingError.set(false)
                isLoading.set(false)
            }.subscribe({
                nextPageToken = it.nextPageToken
                it.results?.let { results ->
                    addPlacesEvent.value = results
                }
            }, {
                Log.e(TAG, it.message)
            })
    }

    companion object {
        private val TAG = MapViewModel::class.java.simpleName
    }
}