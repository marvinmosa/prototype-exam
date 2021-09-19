package com.prototype.exam.di.component

import com.prototype.exam.di.module.*
import com.prototype.exam.ui.main.view.userDetail.UserDetailFragment
import com.prototype.exam.ui.main.view.user.UserFragment
import com.prototype.exam.ui.main.view.UserActivity
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
    fun inject(mainActivity: UserActivity)

    fun inject(forecastDetailFragment: UserDetailFragment)

    fun inject(forecastFragment: UserFragment)
}