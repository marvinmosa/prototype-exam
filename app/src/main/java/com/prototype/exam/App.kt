package com.prototype.exam


import android.app.Application
import com.prototype.exam.di.component.AppComponent
import com.prototype.exam.di.component.DaggerAppComponent
import com.prototype.exam.di.module.AppModule

class App : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this@App))
            .build()
    }
}
