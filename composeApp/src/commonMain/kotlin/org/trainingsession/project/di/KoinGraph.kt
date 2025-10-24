package org.trainingsession.project.di

import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.trainingsession.project.Platform
import org.trainingsession.project.mainScreen.viewModels.ProgramsScreenViewModel

fun initKoin() = startKoin {
    modules(appModule, commonModule)
}

val commonModule = module {
    singleOf(::Platform)
}

val appModule = module {
    viewModel { ProgramsScreenViewModel() }
}