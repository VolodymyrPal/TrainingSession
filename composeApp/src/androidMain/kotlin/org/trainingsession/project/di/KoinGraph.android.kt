package org.trainingsession.project.di

import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.scope.Scope

@Module
actual class NativeModuleD {
//    @Factory
//    actual fun providesPlatformComponentD(scope: Scope): PlatformComponentD =
//        PlatformComponentDiOS()
}
//
//class PlatformComponentDiOS : PlatformComponentD {
//    override fun sayHello(): String = "I'm iOS - D"
//}