package com.test.testapplication.webresponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImagesResponse(
    @SerializedName("pugs")
    @Expose
    var pugs: List<String> = arrayListOf()
)