package com.prototype.exam.di.component

import com.prototype.exam.di.module.AppModule
import com.prototype.exam.di.module.NetworkModule
import com.prototype.exam.di.module.ViewModelModule
import com.prototype.exam.ui.main.view.ForecastDetailFragment
import com.prototype.exam.ui.main.view.ForecastFragment
import com.prototype.exam.ui.main.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, AppModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)

    fun inject(forecastDetailFragment: ForecastDetailFragment)

    fun inject(forecastFragment: ForecastFragment)
}