package com.test.testapplication.data.source.remote

import android.arch.lifecycle.LiveData
import com.test.testapplication.data.Contact
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
constructor(
    private val retrofitServiceFactory: RetrofitServiceFactory
) : DataSource {
    override fun insert(contact: Contact) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllContact(): LiveData<List<Contact>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setAsEmergencyContact(phone: String, isEmergencyContact: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateContact(contact: Contact) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteContactByPhone(phone: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllContact() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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

    private val contactService = retrofitServiceFactory.createContactService()

    override fun getContacts(): Single<List<Contact>> {
        return contactService.getContacts()
            .map {
                for (i in it.indices) {
                    val name = it[i].name ?: ""
                    val list = name.split("\\s".toRegex())
                    it[i].firstName = list[0]
                    it[i].lastName = list[1]
                }

                it
            }
    }

    companion object {
        private val TAG = RemoteDataSource::class.java.simpleName
    }
}