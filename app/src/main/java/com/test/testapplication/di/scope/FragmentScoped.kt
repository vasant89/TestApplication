package com.test.testapplication.di.scope

import javax.inject.Scope


@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
//@kotlin.annotation.Target(AnnotationTarget.TYPE,AnnotationTarget.FUNCTION)
annotation class FragmentScoped