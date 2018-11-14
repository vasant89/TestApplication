package com.test.testapplication.extentions

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.support.annotation.IdRes

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.test.testapplication.ViewModelFactory


fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, @IdRes frameId: Int) {
    supportFragmentManager.transact {
        replace(frameId, fragment)
    }
}

fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}


fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(this.findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

fun AppCompatActivity.hideKeyboard() {
    currentFocus?.let {
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(View(this).windowToken, 0)
    }
}


fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelFactory: ViewModelFactory, viewModelClass: Class<T>) =
    ViewModelProviders.of(this, viewModelFactory).get(viewModelClass)

var alert: AlertDialog? = null

fun AppCompatActivity.showGPSDisabledAlertToUser() {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        Log.e("ActivityExt", "GPS is enabled.")
    } else {
        if (alert == null) {
            val alertDialogBuilder = AlertDialog.Builder(this)

            alertDialogBuilder.setMessage("GPS is disabled in your device. You have to enable gps app needs your current location.")
                .setCancelable(false)
                .setPositiveButton("Goto settings page to enable GPS") { dialog, id ->
                    val callGPSSettingIntent = Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    )
                    startActivity(callGPSSettingIntent)
                }

            alertDialogBuilder.setNegativeButton("Cancel") { dialog, id -> dialog.cancel() }

            alert = alertDialogBuilder.create()
        }

        if (!alert!!.isShowing) {
            alert?.show()
        }
    }
}

fun AppCompatActivity.showActivityClosingDialog() {

    if (alert == null) {
        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setMessage("Do you want to exit from application")
            .setCancelable(false)
            .setPositiveButton("Exit") { dialog, id ->
                dialog.dismiss()
                finish()
            }.setNegativeButton("Cancle") { dialog, id ->
                dialog.dismiss()
            }

        alertDialogBuilder.setNegativeButton("Cancel") { dialog, id -> dialog.cancel() }

        alert = alertDialogBuilder.create()
    }

    if (!alert!!.isShowing) {
        alert?.show()
    }
}