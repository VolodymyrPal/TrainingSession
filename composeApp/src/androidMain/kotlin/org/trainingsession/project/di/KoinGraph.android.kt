package org.trainingsession.project.di

import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.scope.Scope

@Module
actual class NativeModuleD {
    @Factory
    actual fun providesPlatformComponentSample(scope: Scope): PlatformComponentSample =
        PlatformComponentDiOS()
}

class PlatformComponentDiOS : PlatformComponentSample {
}