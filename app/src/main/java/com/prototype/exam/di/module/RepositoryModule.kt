package com.prototype.exam.di.module

import com.prototype.exam.data.api.ApiHelper
import com.prototype.exam.data.db.ForecastDao
import com.prototype.exam.data.repository.Repository
import com.prototype.exam.data.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideRepository(apiHelper: ApiHelper, dao: ForecastDao) : Repository = RepositoryImpl(apiHelper, dao )
}
