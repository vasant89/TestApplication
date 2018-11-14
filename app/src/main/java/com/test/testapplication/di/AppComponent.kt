package com.test.testapplication.di

import android.app.Application
import com.test.testapplication.data.source.Repository
import com.test.testapplication.di.module.ActivityBindingModule
import com.test.testapplication.di.module.ApplicationModule
import com.test.testapplication.di.module.DataModule
import com.test.testapplication.di.module.ViewModelModule
import com.test.testapplication.di.picassomodules.NetworkModule
import com.test.testapplication.di.picassomodules.PicassoModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
        modules = [
            (ApplicationModule::class),
            (AndroidSupportInjectionModule::class),
            (ActivityBindingModule::class),
            (PicassoModule::class),
            (NetworkModule::class),
            (DataModule::class),
            (ViewModelModule::class)
        ]
)
interface AppComponent : AndroidInjector<App> {

    fun getRepository(): Repository

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}