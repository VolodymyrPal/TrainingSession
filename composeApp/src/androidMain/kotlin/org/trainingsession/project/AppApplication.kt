package org.trainingsession.project

import android.app.Application
import org.trainingsession.project.di.initKoin

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}