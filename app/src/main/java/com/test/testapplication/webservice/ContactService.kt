package com.test.testapplication.webservice

import com.test.testapplication.data.Contact
import io.reactivex.Single
import retrofit2.http.GET

interface ContactService{

    @GET("json/contacts.json")
    fun getContacts(): Single<List<Contact>>
}