package org.trainingsession.project.utils

import io.ktor.client.plugins.logging.Logger

expect object AppLogger {
    fun e(tag: String, message: String, throwable: Throwable?)
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
}

private const val KTOR_TAG = "KtorClient"

object MyKtorLogger : Logger {
    override fun log(message: String) {
        AppLogger.d(KTOR_TAG, message)
    }
}