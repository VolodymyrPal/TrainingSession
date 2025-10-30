package org.trainingsession.project.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.core.logger.Level
import org.koin.core.scope.Scope
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.ksp.generated.startKoin
import org.trainingsession.project.utils.MyKtorLogger

@KoinApplication(modules = [CommonModule::class])
object MyApp

@Module(includes = [NativeModuleD::class])
@ComponentScan("org.trainingsession.project")
class CommonModule {

    @Factory
    fun provideHttpClient(): HttpClient = HttpClient() {
        install(DefaultRequest) {

        }
        install(Logging) {
            logger = MyKtorLogger
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                    prettyPrint = true
                }
            )
        }
        install(HttpCache)
    }

}

@Module
expect class NativeModuleD() {
    @Factory
    fun providesPlatformComponentSample(scope: Scope): PlatformComponentSample
}

interface PlatformComponentSample {

}

fun initKoin(config: KoinAppDeclaration? = null) {
    MyApp.startKoin {
        printLogger(Level.DEBUG)
        config?.invoke(this)
    }
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