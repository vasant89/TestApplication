package com.test.testapplication.main

import com.kaopiz.kprogresshud.KProgressHUD
import com.test.testapplication.di.scope.FragmentScoped
import com.test.testapplication.main.photos.PhotoView
import com.test.testapplication.main.searchplace.SearchPlaceView
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
abstract class MainModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun mainView(): SearchPlaceView

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun photoView(): PhotoView

    @Module
    companion object {

        @JvmStatic
        @Singleton
        @Provides
        fun provideProgressDialog(activity: MainActivity): KProgressHUD {
            return KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)
        }
    }
}