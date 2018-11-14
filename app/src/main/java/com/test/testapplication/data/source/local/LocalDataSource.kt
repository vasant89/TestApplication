package com.test.testapplication.data.source.local


import com.test.testapplication.data.source.DataSource
import com.test.testapplication.utils.AppExecutors
import com.test.testapplication.webresponse.ImagesResponse
import com.test.testapplication.webresponse.PlaceResponse
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocalDataSource
@Inject
constructor(
    private val appExecutors: AppExecutors,
    private val contactDao: ContactDao
) : DataSource {
    override fun getPlaceDetail(placeId: String, radius: Double, key: String): Single<PlaceResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPlaceDetailNextPage(pageToken: String, key: String): Single<PlaceResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getImages(): Single<ImagesResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
