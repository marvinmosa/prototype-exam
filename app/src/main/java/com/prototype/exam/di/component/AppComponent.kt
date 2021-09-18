package com.prototype.exam.di.component

import com.prototype.exam.di.module.*
import com.prototype.exam.ui.main.view.ForecastDetailFragment
import com.prototype.exam.ui.main.view.ForecastFragment
import com.prototype.exam.ui.main.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ViewModelModule::class,
    AppModule::class,
    NetworkModule::class,
    RepositoryModule::class,
    DatabaseModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)

    fun inject(forecastDetailFragment: ForecastDetailFragment)

    fun inject(forecastFragment: ForecastFragment)
}