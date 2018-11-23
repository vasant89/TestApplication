package com.test.testapplication.extentions

import android.content.Context
import android.graphics.Point
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Toast
import java.lang.Exception
import android.support.v4.content.ContextCompat.startActivity
import android.net.Uri.fromParts
import android.content.Intent
import android.net.Uri
import android.util.TypedValue




fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun Context.showMessage(message: String) {
    val handler = Handler(Looper.getMainLooper())
    handler.post { Toast.makeText(this, message, Toast.LENGTH_LONG).show() }
}

fun Context.call(mobile: String) {
    Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse(mobile)
        startActivity(this)
    }
}

fun Context.getScreenWidth(): Int {
    val columnWidth: Int
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay

    val point = Point()
    try {
        display.getSize(point)
    } catch (ignore: java.lang.NoSuchMethodError) { // Older device
        point.x = display.width
        point.y = display.height
    }

    columnWidth = point.x
    return columnWidth
}

fun Context.copyText(string: String, message: String) {
    try {
        val sdk = android.os.Build.VERSION.SDK_INT
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.text.ClipboardManager
            clipboard.text = string
        } else {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("WordKeeper", string)
            clipboard.primaryClip = clip
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.dpToPx(dp: Int): Int {
    val r = resources
    return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(), r.displayMetrics
    ))
}