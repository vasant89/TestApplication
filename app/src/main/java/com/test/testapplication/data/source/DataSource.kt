package com.test.testapplication.data.source

import android.arch.lifecycle.LiveData
import com.test.testapplication.data.Contact
import com.test.testapplication.webresponse.ImagesResponse
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

    fun getImages(): Single<ImagesResponse>

    fun getContacts(): Single<List<Contact>>

    fun insert(contact: Contact)

    fun getAllContact(): LiveData<List<Contact>>

    fun updateContact(contact: Contact)

    fun setAsEmergencyContact(phone: String, isEmergencyContact: Boolean)

    fun deleteContactByPhone(phone: String)

    fun deleteAllContact()
}