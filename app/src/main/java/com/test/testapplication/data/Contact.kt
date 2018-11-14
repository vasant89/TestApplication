package com.test.testapplication.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity
data class Contact(
    var name: String? = "",
    var mobile: String? = ""
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}