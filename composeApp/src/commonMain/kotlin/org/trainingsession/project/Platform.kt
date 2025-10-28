package org.trainingsession.project

import org.koin.core.annotation.Factory

@Factory
expect class Platform() {
    val name: String
}