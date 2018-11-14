package com.test.testapplication.location

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.widget.Toast



import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File


class PermissionHelper {

    fun checkPermission(activity: Activity, stringPermission: String, message: String, permissionInterface: PermissionInterface) {
        with(permissionInterface) {
            if (Build.VERSION.SDK_INT < 23) {
                granted(true)
            } else {
                if (ContextCompat.checkSelfPermission(activity, stringPermission) == PackageManager.PERMISSION_GRANTED) {
                    granted(true)
                } else {
                    Dexter.withActivity(activity)
                            .withPermission(stringPermission)
                            .withListener(object : PermissionListener {
                                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                                    granted(true)
                                }

                                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                                    token!!.continuePermissionRequest()
                                }

                                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                                    granted(false)
                                    if (response!!.isPermanentlyDenied) {
                                        showSettingsDialog(activity, "$message. You can grant them in app settings.")
                                    } else {
                                        showMessageDialog(activity, message)
                                    }

                                }

                            })
                            .withErrorListener {
                                Toast.makeText(activity, "Error occurred! ", Toast.LENGTH_SHORT).show();
                            }
                            .onSameThread()
                            .check()
                }
            }
        }
    }

    private fun showMessageDialog(activity: Activity, message: String) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Need Permissions")
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun showSettingsDialog(activity: Activity, message: String) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Need Permissions")
        builder.setMessage(message)
        builder.setPositiveButton("GOTO SETTINGS") { dialog, _ ->
            dialog.cancel()
            openSettings(activity)
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()

    }

    // navigating user to app settings
    fun openSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivityForResult(intent, 101)
    }

    interface PermissionInterface {
        fun granted(granted: Boolean)
    }

    companion object {
        private val TAG = PermissionHelper::class.java.simpleName
        private val RC_SAVE_PROFILE_PICTURE = 1
    }


}