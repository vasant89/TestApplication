package com.test.testapplication.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("location")
    @Expose
    var location: Location? = null,
    @SerializedName("viewport")
    @Expose
    var viewport: Viewport? = null
)