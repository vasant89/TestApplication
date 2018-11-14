package com.test.testapplication.di.module


import com.test.testapplication.main.MainActivity
import com.test.testapplication.di.scope.ActivityScoped
import com.test.testapplication.main.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun mainActivity(): MainActivity

}