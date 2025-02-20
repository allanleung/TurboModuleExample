package com.nativebatteryinfo

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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

    override fun getName(): String = NAME

    override fun getBatteryLevel(promise: Promise) {
        try {
            val batteryManager =
                    reactContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val batteryLevel =
                    batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            promise.resolve(batteryLevel)
        } catch (e: Exception) {
            promise.reject("ERROR", "Could not get battery level", e)
        }
    }

    override fun getBatteryHealth(promise: Promise) {
        try {
            val batteryIntent: Intent? =
                    IntentFilter(Intent.ACTION_BATTERY_CHANGED).let {
                        reactContext.registerReceiver(null, it)
                    }
            // EXTRA_HEALTH returns an int representing health state (e.g., BATTERY_HEALTH_GOOD)
            val health = batteryIntent?.getIntExtra(BatteryManager.EXTRA_HEALTH, -1) ?: -1
            promise.resolve(health)
        } catch (e: Exception) {
            promise.reject("ERROR", "Could not get battery health", e)
        }
    }

    override fun getBatteryCapacity(promise: Promise) {
        try {
            // Use reflection to access the hidden PowerProfile API for battery capacity (in mAh)
            val powerProfileClass = Class.forName("com.android.internal.os.PowerProfile")
            val constructor = powerProfileClass.getConstructor(Context::class.java)
            val powerProfile = constructor.newInstance(reactContext)
            val method = powerProfileClass.getMethod("getBatteryCapacity")
            val capacity = method.invoke(powerProfile) as Double
            promise.resolve(capacity)
        } catch (e: Exception) {
            promise.reject("ERROR", "Could not get battery capacity", e)
        }
    }

    override fun getBatteryChargeVoltage(promise: Promise) {
        try {
            val batteryIntent: Intent? =
                    IntentFilter(Intent.ACTION_BATTERY_CHANGED).let {
                        reactContext.registerReceiver(null, it)
                    }
            // EXTRA_VOLTAGE is in millivolts
            val voltage = batteryIntent?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) ?: -1
            promise.resolve(voltage)
        } catch (e: Exception) {
            promise.reject("ERROR", "Could not get battery voltage", e)
        }
    }

    override fun getBatteryChargeCycle(promise: Promise) {
        try {
            // There is no standard public API for battery charge cycle.
            // On some devices this might be exposed via a sysfs file.
            // We'll attempt to read it; if unavailable, return -1.
            val cycleCount =
                    try {
                        val file = java.io.File("/sys/class/power_supply/battery/cycle_count")
                        if (file.exists()) {
                            val text = file.readText().trim()
                            text.toIntOrNull() ?: -1
                        } else {
                            -1
                        }
                    } catch (ex: Exception) {
                        -1
                    }
            promise.resolve(cycleCount)
        } catch (e: Exception) {
            promise.reject("ERROR", "Could not get battery charge cycle", e)
        }
    }
}
