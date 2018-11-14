package com.test.testapplication.extentions

import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.widget.Toast

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun Context.showMessage(message: String) {
    val handler = Handler(Looper.getMainLooper())
    handler.post { Toast.makeText(this, message, Toast.LENGTH_LONG).show() }
}