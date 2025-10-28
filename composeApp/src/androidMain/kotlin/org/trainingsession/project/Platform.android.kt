package org.trainingsession.project

import android.os.Build
import org.koin.core.annotation.Factory

@Factory
actual class Platform actual constructor() {
    actual val name: String = "Android ${Build.VERSION.SDK_INT}"
}

