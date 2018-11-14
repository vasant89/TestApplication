package com.test.testapplication

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.google.android.gms.common.api.GoogleApiClient
import com.test.testapplication.data.source.Repository
import com.test.testapplication.main.MainViewModel
import com.test.testapplication.main.map.MapViewModel


class ViewModelFactory(
    private val application: Application,
    private val repository: Repository,
    private val picasso: Picasso
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(application, picasso)
                isAssignableFrom(MapViewModel::class.java) ->
                    MapViewModel(application, repository, picasso)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}