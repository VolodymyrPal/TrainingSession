//
//  NativeActionBridge.swift
//  iosApp
//
//  Created by Palamarchuk Volodymyr on 25.10.2025.
//
import ComposeApp
import Foundation

@objc(NativeActionBridge)
public class NativeActionBridge: NSObject {
    @objc public static func doWork() {
        print("Swift Code: NativeActionBridge.doWork() был вызван!")
    }
}
