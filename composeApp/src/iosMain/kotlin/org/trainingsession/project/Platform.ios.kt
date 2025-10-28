package org.trainingsession.project

import org.koin.core.annotation.Factory
import platform.UIKit.UIDevice

@Factory
actual class Platform {
    actual val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

private var nativeWorkLambda: (() -> Unit)? = null

fun setNativeWorkLambda(lambda: () -> Unit) {
    nativeWorkLambda = lambda
}

