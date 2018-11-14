package com.test.testapplication.data.source

import com.test.testapplication.webresponse.PlaceResponse
import io.reactivex.Single


interface DataSource {
    fun getPlaceDetail(
        placeId: String,
        radius: Double,
        key: String
    ): Single<PlaceResponse>


    fun getPlaceDetailNextPage(
        pageToken: String,
        key: String
    ): Single<PlaceResponse>
}