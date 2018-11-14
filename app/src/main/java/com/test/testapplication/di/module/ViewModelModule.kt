package com.test.testapplication.di.module

import android.app.Application
import android.content.Context
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.squareup.picasso.Picasso
import com.test.testapplication.ViewModelFactory
import com.test.testapplication.data.source.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ViewModelModule {

    @Singleton
    @Provides
    fun provideViewModelFactory(context: Application, repository: Repository, picasso: Picasso ): ViewModelFactory {
        return ViewModelFactory(context,repository, picasso)
    }
}