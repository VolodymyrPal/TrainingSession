package org.trainingsession.project

import android.app.Application

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}