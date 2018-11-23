package com.test.testapplication.di.picassomodules


import android.content.Context

import java.io.File

import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import javax.inject.Singleton


@Module // what ever dependency you need form other module add here
class NetworkModule {

    @Singleton
    @Provides
    fun provideCacheFile(context: Context): File {
        return File(context.cacheDir, "okhttp_cache")
    }

    @Provides
    fun provideCache(cacheFile: File): Cache {
        return Cache(cacheFile, 10 * 1000 * 1000) // creating 10Mb cache
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
                .cache(cache)
                .build()
        /* we are providing cache file for okhttp because
         it will store network call or image, by this way we can
         optimise the network efficiency  */
    }
}