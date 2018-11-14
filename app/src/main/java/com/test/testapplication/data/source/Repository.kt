package com.test.testapplication.data.source


import com.test.testapplication.di.scope.Local
import com.test.testapplication.di.scope.Remote
import com.test.testapplication.webresponse.PlaceResponse
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Repository
@Inject
constructor(
    @Local private val localDataSource: DataSource,
    @Remote private val remoteDataSource: DataSource
) : DataSource {

    override fun getPlaceDetail(placeId: String, radius: Double, key: String): Single<PlaceResponse> {
        return remoteDataSource.getPlaceDetail(placeId, radius, key)
    }

    override fun getPlaceDetailNextPage(pageToken: String, key: String): Single<PlaceResponse> {
        return remoteDataSource.getPlaceDetailNextPage(pageToken, key)
    }

}