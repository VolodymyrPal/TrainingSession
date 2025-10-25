import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        Platform_iosKt.setNativeWorkLambda {
            NativeActionBridge.doWork()
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}