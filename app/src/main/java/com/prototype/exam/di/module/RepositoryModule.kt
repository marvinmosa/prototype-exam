package com.prototype.exam.di.module

import android.content.SharedPreferences
import com.prototype.exam.data.api.ApiHelper
import com.prototype.exam.data.db.LoginDao
import com.prototype.exam.data.db.UserDao
import com.prototype.exam.data.repository.LoginRepository
import com.prototype.exam.data.repository.LoginRepositoryImpl
import com.prototype.exam.data.repository.UserRepository
import com.prototype.exam.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideRepository(apiHelper: ApiHelper, dao: UserDao) : UserRepository = UserRepositoryImpl(apiHelper, dao )

    @Singleton
    @Provides
    fun provideLoginRepository(dao: LoginDao, loginSharedPref: SharedPreferences) : LoginRepository = LoginRepositoryImpl(dao, loginSharedPref)
}
