package org.trainingsession.project.di

import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.koin.ksp.generated.startKoin
import org.trainingsession.project.Platform
import org.trainingsession.project.presentation.viewModels.ProgramsScreenViewModel

fun initKoin(config: KoinAppDeclaration? = null) {
    MyApp.startKoin {
        config?.invoke(this)
        modules(commonModule, CommonModule().module)
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