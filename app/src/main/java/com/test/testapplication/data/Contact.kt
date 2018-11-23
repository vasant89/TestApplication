package com.test.testapplication.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity
data class Contact(
    var image: String = "",
    var name: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var phone: String = "",
    var isEmergencyContact: Boolean = false
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}