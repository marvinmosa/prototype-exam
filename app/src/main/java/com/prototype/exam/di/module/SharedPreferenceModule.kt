package com.prototype.exam.di.module

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.prototype.exam.utils.Constants.SHARED_PREFERENCE_LOGIN
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class SharedPreferenceModule {
    @Singleton
    @Provides
    fun provideLoginSharedPref(context: Context): SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_LOGIN, MODE_PRIVATE)
}
