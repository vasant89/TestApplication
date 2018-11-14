package com.test.testapplication.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.test.testapplication.data.Contact

@Database(entities = [(Contact::class)], version = 1)
abstract class TestApplicationDB : RoomDatabase() {

    abstract fun contactDao(): ContactDao

}