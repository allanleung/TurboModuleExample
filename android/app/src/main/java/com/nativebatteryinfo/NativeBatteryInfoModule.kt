package com.nativebatteryinfo

import android.content.Context
import android.os.BatteryManager
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule

@ReactModule(name = NativeBatteryInfoModule.NAME)
class NativeBatteryInfoModule(private val reactContext: ReactApplicationContext) :
        NativeBatteryInfoSpec(reactContext) {
    companion object {
        const val NAME = "NativeBatteryInfo"
    }

    override fun getName(): String {
        return NAME
    }

    override fun getBatteryLevel(promise: Promise) {
        try {
            // Get the battery service and fetch battery percentage
            val batteryManager =
                    reactContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val batteryLevel =
                    batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            promise.resolve(batteryLevel)
        } catch (e: Exception) {
            promise.reject("ERROR", "Could not get battery level", e)
        }
    }
}
