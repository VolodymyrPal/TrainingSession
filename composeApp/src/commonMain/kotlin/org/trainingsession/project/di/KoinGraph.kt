package org.trainingsession.project.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.core.logger.Level
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.ksp.generated.startKoin

fun initKoin(config: KoinAppDeclaration? = null) {
    MyApp.startKoin {
        printLogger(Level.DEBUG)
        config?.invoke(this)
    }
}

@KoinApplication(modules = [CommonModule::class])
object MyApp

@Module
class CommonModule {

    @Single
    fun platform(): Platform = Platform()
}

val commonModule = module {
    viewModelOf(::ProgramsScreenViewModel)
    singleOf(::Platform)
}

fun initKoinWithIos(config: KoinAppDeclaration? = null, iosDependencies: IOSDependencies) {
    initKoin {
        config?.invoke(this)
        modules(
            platformModule(iosDependencies)
        )
    }
}

interface IOSDependencies {}

fun platformModule(deps: IOSDependencies) = module {
    single<IOSDependencies> { deps }
}