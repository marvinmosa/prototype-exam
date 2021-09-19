package com.prototype.exam.di.module

import android.content.Context
import androidx.annotation.NonNull
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(@NonNull context: Context) {
    private val  applicationContext: Context = context

    @Singleton
    @Provides
    @NonNull
    fun provideApplicationContext(): Context {
        return applicationContext
    }
}