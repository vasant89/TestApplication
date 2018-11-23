package com.test.testapplication.data.source


import android.arch.lifecycle.LiveData
import com.test.testapplication.data.Contact
import com.test.testapplication.di.scope.Local
import com.test.testapplication.di.scope.Remote
import com.test.testapplication.webresponse.ImagesResponse
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
    override fun insert(contact: Contact) {
        localDataSource.insert(contact)
    }

    override fun getAllContact(): LiveData<List<Contact>> {
        return localDataSource.getAllContact()
    }

    override fun updateContact(contact: Contact) {
        localDataSource.updateContact(contact)
    }
    override fun setAsEmergencyContact(phone: String, isEmergencyContact: Boolean) {
        localDataSource.setAsEmergencyContact(phone, isEmergencyContact)
    }

    override fun deleteContactByPhone(phone: String) {
        localDataSource.deleteContactByPhone(phone)
    }

    override fun deleteAllContact() {
        localDataSource.deleteAllContact()
    }

    override fun getPlaceDetail(placeId: String, radius: Double, key: String): Single<PlaceResponse> {
        return remoteDataSource.getPlaceDetail(placeId, radius, key)
    }

    override fun getPlaceDetailNextPage(pageToken: String, key: String): Single<PlaceResponse> {
        return remoteDataSource.getPlaceDetailNextPage(pageToken, key)
    }

    override fun getImages(): Single<ImagesResponse> {
        return remoteDataSource.getImages()
    }

    override fun getContacts(): Single<List<Contact>> {
        return remoteDataSource.getContacts()
    }
}