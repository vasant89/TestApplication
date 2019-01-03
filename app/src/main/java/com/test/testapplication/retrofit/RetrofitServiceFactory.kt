package com.test.testapplication.retrofit


import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import android.util.Log
import com.test.testapplication.extentions.isInternetAvailable
import com.test.testapplication.extentions.showMessage
import com.test.testapplication.webservice.ContactService
import com.test.testapplication.webservice.PhotoService
import com.test.testapplication.webservice.PlaceService
import javax.inject.Singleton

@Singleton
class RetrofitServiceFactory
@Inject
constructor(private val context: Context) {

    private val TAG = RetrofitServiceFactory::class.java.simpleName

    private var client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .addInterceptor { chain ->

            if (!context.isInternetAvailable()) {
                Log.e(TAG, "No Internet!")
                context.showMessage("No Internet!")
            }

            val original: Request = chain.request()

            val request: Request = original.newBuilder()
                .header("x-api-key", "abcd1234")
                .method(original.method(), original.body())
                .build()

            chain.proceed(request)

        }.connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .build()



    private var retrofitForGoogle: Retrofit = Retrofit.Builder()
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://maps.googleapis.com/maps/api/place/")
        .build()

    private var retrofitForPhotos: Retrofit = Retrofit.Builder()
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://private-e1b8f4-getimages.apiary-mock.com/")
        .build()

    private var retrofitForContacts: Retrofit = Retrofit.Builder()
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.androidhive.info/")
        .build()

    fun createPlaceService(): PlaceService {
        return retrofitForGoogle.create(PlaceService::class.java)
    }

    fun createPhotoService():PhotoService{
        return retrofitForPhotos.create(PhotoService::class.java)
    }

    fun createContactService():ContactService{
        return retrofitForContacts.create(ContactService::class.java)
    }

}