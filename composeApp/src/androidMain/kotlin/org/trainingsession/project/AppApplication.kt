package org.trainingsession.project

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.trainingsession.project.di.appModule
import org.trainingsession.project.di.commonModule

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppApplication)
            modules(appModule, commonModule)
        }
    }
}