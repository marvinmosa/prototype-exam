package com.prototype.exam.di.module

import android.content.Context
import androidx.annotation.NonNull
import com.prototype.exam.data.api.ApiHelper
import com.prototype.exam.data.db.AppDatabase
import com.prototype.exam.data.db.ForecastDao
import com.prototype.exam.data.repository.RepositoryImpl
import com.prototype.exam.utils.NetworkHelper
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