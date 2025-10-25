package org.trainingsession.project

expect class Platform() {
    val name: String
}

expect fun someWork()