package org.trainingsession.project.di

import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.trainingsession.project.Platform
import org.trainingsession.project.presentation.viewModels.ProgramsScreenViewModel

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, commonModule, CommonModule().module)
    }
}

fun initKoin() = startKoin {
    modules(appModule, commonModule)
}

val commonModule = module {
    singleOf(::Platform)
}

val appModule = module {
    viewModel { ProgramsScreenViewModel() }
}