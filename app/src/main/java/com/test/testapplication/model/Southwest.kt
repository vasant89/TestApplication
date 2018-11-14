package com.test.testapplication.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Southwest(
    @SerializedName("lat")
    @Expose
    var lat: Double? = null,
    @SerializedName("lng")
    @Expose
    var lng: Double? = null
)