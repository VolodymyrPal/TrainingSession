package org.trainingsession.project

actual class Platform actual constructor() {
    actual val name: String = "Java ${System.getProperty("java.version")}"
}