package com.test.testapplication.data.source.remote

import com.test.testapplication.data.source.DataSource
import com.test.testapplication.retrofit.RetrofitServiceFactory
import com.test.testapplication.webresponse.ImagesResponse
import com.test.testapplication.webresponse.PlaceResponse
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource
@Inject
constructor(private val retrofitServiceFactory: RetrofitServiceFactory) : DataSource {

    private val placeService = retrofitServiceFactory.createPlaceService()

    override fun getPlaceDetail(placeId: String, radius: Double, key: String): Single<PlaceResponse> {
        return placeService.getPlaceDetail(placeId, radius, key)
    }

    override fun getPlaceDetailNextPage(pageToken: String, key: String): Single<PlaceResponse> {
        return placeService.getPlaceDetailNextPage(pageToken, key)
    }

    private val photoService = retrofitServiceFactory.createPhotoService()

    override fun getImages(): Single<ImagesResponse> {
        return photoService.getImages()
    }

    companion object {
        private val TAG = RemoteDataSource::class.java.simpleName
    }
}