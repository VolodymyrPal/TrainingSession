package org.trainingsession.project

import platform.UIKit.UIDevice

actual class Platform actual constructor() {
    actual val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

private var nativeWorkLambda: (() -> Unit)? = null

fun setNativeWorkLambda(lambda: () -> Unit) {
    nativeWorkLambda = lambda
}
