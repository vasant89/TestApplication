package com.test.testapplication.di.picassomodules


import android.content.Context

import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton


@Module // what ever dependency you need form other module add here
class PicassoModule {

    @Singleton
    @Provides
    fun provideOkHttp3Downloder(okHttpClient: OkHttpClient): OkHttp3Downloader {
        return OkHttp3Downloader(okHttpClient)

        /*  if you want that both retrofit and picasso will call through OkHttpClient
         then use it so that both can share same cache file
         otherwise directly use following provider to get Picasso, picasso will have
         there own caching mechanism  */
    }

    @Singleton
    @Provides
    fun providePicasso(context: Context, okHttp3Downloader: OkHttp3Downloader): Picasso {
        return Picasso.Builder(context)
                .downloader(okHttp3Downloader)
                .build()
    }

}