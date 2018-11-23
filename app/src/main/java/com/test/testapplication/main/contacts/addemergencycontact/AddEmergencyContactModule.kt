package com.test.testapplication.main.contacts.addemergencycontact

import com.kaopiz.kprogresshud.KProgressHUD
import com.test.testapplication.di.scope.ActivityScoped
import com.test.testapplication.di.scope.FragmentScoped
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class AddEmergencyContactModule{

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun addEmergencyContactView(): AddEmergencyContactView

    @Module
    companion object {

        @JvmStatic
        @Provides
        @ActivityScoped
        fun provideProgressDialog(activity: AddEmergencyContactActivity): KProgressHUD {
            return KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(1)
                .setDimAmount(0.5f)

        }
    }
}