package com.prototype.exam.di.component

import com.prototype.exam.di.module.*
import com.prototype.exam.ui.main.view.login.LoginActivity
import com.prototype.exam.ui.main.view.splash.SplashActivity
import com.prototype.exam.ui.main.view.user.UserActivity
import com.prototype.exam.ui.main.view.user.UserFragment
import com.prototype.exam.ui.main.view.userDetail.UserDetailFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ViewModelModule::class,
        AppModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        DatabaseModule::class,
        SharedPreferenceModule::class]
)
interface AppComponent {
    fun inject(mainActivity: UserActivity)

    fun inject(activity: LoginActivity)

    fun inject(activity: SplashActivity)

    fun inject(forecastDetailFragment: UserDetailFragment)

    fun inject(forecastFragment: UserFragment)
}