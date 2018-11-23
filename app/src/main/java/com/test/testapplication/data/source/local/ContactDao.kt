package com.test.testapplication.data.source.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.test.testapplication.data.Contact


@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: Contact)

    @Query("SELECT * from Contact ORDER BY firstName ASC")
    fun getAllContact(): LiveData<List<Contact>>

    @Query("UPDATE Contact SET isEmergencyContact = :isEmergencyContact WHERE phone = :phone")
    fun setAsEmergencyContact(phone: String, isEmergencyContact: Boolean)

    @Query("DELETE FROM Contact WHERE phone = :phone")
    fun deleteContactByPhone(phone: String)

    @Update
    fun updateContact(contact: Contact)

    @Query("DELETE FROM Contact")
    fun deleteAllContact()
}