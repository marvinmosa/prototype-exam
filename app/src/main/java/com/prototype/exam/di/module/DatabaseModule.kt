package com.prototype.exam.di.module

import android.content.Context
import com.prototype.exam.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(context: Context) = AppDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase) = db.userDao()

    @Singleton
    @Provides
    fun provideLoginDao(db: AppDatabase) = db.loginDao()
}