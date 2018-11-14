package com.test.testapplication.webservice

import com.test.testapplication.webresponse.PlaceResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface PlaceService {

    @GET("nearbysearch/json?")
    fun getPlaceDetail(
        @Query("location") location: String,
        @Query("radius") radius: Double,
        @Query("key") key: String
    ): Single<PlaceResponse>

    @GET("nearbysearch/json?")
    fun getPlaceDetailNextPage(
        @Query("pagetoken") pageToken: String,
        @Query("key") key: String
    ): Single<PlaceResponse>
}