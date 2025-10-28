package org.trainingsession.project

import org.koin.core.annotation.Factory

@Factory
actual class Platform actual constructor() {
    actual val name: String = "Java ${System.getProperty("java.version")}"
}
