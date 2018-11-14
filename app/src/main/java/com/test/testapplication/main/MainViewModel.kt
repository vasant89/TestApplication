package com.test.testapplication.main

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import android.databinding.ObservableBoolean
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.test.testapplication.SingleLiveEvent

class MainViewModel(
    context: Application,
    picasso: Picasso
) : AndroidViewModel(context) {

    @SuppressLint("StaticFieldLeak")
    private val context: Context = context.applicationContext //Application Context to avoid leaks.

    val isDataLoading = SingleLiveEvent<Boolean>()
    val isDataLoadingError = ObservableBoolean(false)

    val empty = ObservableBoolean(false)

    val showToastMessage = SingleLiveEvent<String>()

    private fun showToastMassage(message: String) {
        showToastMessage.value = message
    }

    fun start() {

    }


    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }
}