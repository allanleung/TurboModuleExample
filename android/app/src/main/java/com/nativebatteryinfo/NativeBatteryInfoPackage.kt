package com.nativebatteryinfo

import com.facebook.react.BaseReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider

class NativeBatteryInfoPackage : BaseReactPackage() {

    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? =
            if (name == NativeBatteryInfoModule.NAME) {
                NativeBatteryInfoModule(reactContext)
            } else {
                null
            }

    override fun getReactModuleInfoProvider() = ReactModuleInfoProvider {
        mapOf(
                NativeBatteryInfoModule.NAME to
                        ReactModuleInfo(
                                _name = NativeBatteryInfoModule.NAME,
                                _className = NativeBatteryInfoModule.NAME,
                                _canOverrideExistingModule = false,
                                _needsEagerInit = false,
                                isCxxModule = false,
                                isTurboModule = true
                        )
        )
    }
}
