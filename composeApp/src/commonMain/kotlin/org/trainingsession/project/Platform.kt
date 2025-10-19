package org.trainingsession.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform