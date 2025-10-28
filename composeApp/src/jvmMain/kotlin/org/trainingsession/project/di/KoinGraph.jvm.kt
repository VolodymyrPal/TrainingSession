package org.trainingsession.project.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
actual class NativeModuleD {
//    @Factory
//    actual fun providesPlatformComponentD(scope: Scope): PlatformComponentD =
//        PlatformComponentDiDesktop()
}

//class PlatformComponentDiDesktop : PlatformComponentD {
//    override fun sayHello(): String = "I'm Desktop - D"
//}