package com.prototype.exam.di.module

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prototype.exam.data.db.AppDatabase
import com.prototype.exam.ui.main.viewModel.ForecastDetailViewModel
import com.prototype.exam.ui.main.viewModel.ForecastViewModel
import com.prototype.exam.utils.ViewModelFactory
import com.prototype.exam.utils.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(context: Context) = AppDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideForecastDao(db: AppDatabase) = db.forecastDao()
}