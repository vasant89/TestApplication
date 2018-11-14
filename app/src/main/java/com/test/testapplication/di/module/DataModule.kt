package com.test.testapplication.di.module

import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import android.app.Application
import com.test.testapplication.data.source.DataSource
import com.test.testapplication.data.source.local.ContactDao
import com.test.testapplication.data.source.local.LocalDataSource
import com.test.testapplication.data.source.local.TestApplicationDB
import com.test.testapplication.data.source.remote.RemoteDataSource
import com.test.testapplication.di.scope.Local
import com.test.testapplication.di.scope.Remote
import com.test.testapplication.retrofit.RetrofitServiceFactory
import com.test.testapplication.utils.AppExecutors
import com.test.testapplication.utils.DiskIOThreadExecutor
import java.util.concurrent.Executors


@Module
class DataModule {

    private val THREAD_COUNT = 3

    @Singleton
    @Provides
    @Local
    fun provideTasksLocalDataSource(executors: AppExecutors, contactDao: ContactDao): DataSource {
        return LocalDataSource(executors, contactDao)
    }

    @Singleton
    @Provides
    @Remote
    fun provideTasksRemoteDataSource(context: Application): DataSource {
        return RemoteDataSource(RetrofitServiceFactory(context.applicationContext))
    }

    @Singleton
    @Provides
    fun provideDb(context: Application): TestApplicationDB {
        return Room.databaseBuilder(context.applicationContext, TestApplicationDB::class.java, "TestAppDB.db")
            .build()
    }


    @Singleton
    @Provides
    fun provideContactDao(db: TestApplicationDB): ContactDao {
        return db.contactDao()
    }

    @Singleton
    @Provides
    fun provideAppExecutors(): AppExecutors {
        return AppExecutors(
            DiskIOThreadExecutor(),
            Executors.newFixedThreadPool(THREAD_COUNT),
            AppExecutors.MainThreadExecutor()
        )
    }

}