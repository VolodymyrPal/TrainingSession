import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        let deps = IOSDependenciesImpl()
        KoinGraphKt.doInitKoinWithIos(
            config: nil,
            iosDependencies: deps
        )
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

public class IOSDependenciesImpl: NSObject, IOSDependencies {
}