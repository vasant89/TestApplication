package com.test.testapplication.webservice

import com.test.testapplication.webresponse.ImagesResponse
import io.reactivex.Single
import retrofit2.http.GET

interface PhotoService{
    @GET("getImages")
    fun getImages(): Single<ImagesResponse>
}