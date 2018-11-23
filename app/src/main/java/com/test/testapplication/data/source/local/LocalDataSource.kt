package com.test.testapplication.data.source.local


import android.arch.lifecycle.LiveData
import com.test.testapplication.data.Contact
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

    override fun insert(contact: Contact) {
        appExecutors.diskIO.execute {
            contactDao.insert(contact)
        }
    }

    override fun getAllContact(): LiveData<List<Contact>> {
        return contactDao.getAllContact()
    }

    override fun updateContact(contact: Contact) {
        appExecutors.diskIO.execute{
            contactDao.updateContact(contact)
        }
    }

    override fun setAsEmergencyContact(phone: String, isEmergencyContact: Boolean) {
        appExecutors.diskIO.execute{
            contactDao.setAsEmergencyContact(phone,isEmergencyContact)
        }
    }

    override fun deleteContactByPhone(phone: String) {
        appExecutors.diskIO.execute {
            contactDao.deleteContactByPhone(phone)
        }
    }

    override fun deleteAllContact() {
        appExecutors.diskIO.execute {
            contactDao.getAllContact()
        }
    }

    override fun getPlaceDetail(placeId: String, radius: Double, key: String): Single<PlaceResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPlaceDetailNextPage(pageToken: String, key: String): Single<PlaceResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getImages(): Single<ImagesResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getContacts(): Single<List<Contact>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
