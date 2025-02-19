package com.nativebattery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.nativebattery.NativeBatterySpec

class NativeBatteryModule(reactContext: ReactApplicationContext) : NativeBatterySpec(reactContext) {

  private var batteryReceiver: BroadcastReceiver? = null

  override fun getName() = NAME

  /**
   * Retrieves the current battery level as a percentage.
   */
  override fun getBatteryLevel(): Double {
    val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    val batteryStatus: Intent? = reactApplicationContext.registerReceiver(null, intentFilter)
    val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    return if (level == -1 || scale == -1) 0.0 else level * 100.0 / scale
  }

  /**
   * Subscribes to battery change events.
   * The provided callback is invoked with the updated battery percentage.
   */
  override fun subscribeToBatteryChange(callback: (Double) -> Unit) {
    // Avoid registering more than once.
    if (batteryReceiver != null) return

    batteryReceiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BATTERY_CHANGED) {
          val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
          val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
          val batteryPct = if (level == -1 || scale == -1) 0.0 else level * 100.0 / scale
          // Invoke the JS callback with the new battery level.
          callback(batteryPct)
          // Additionally, emit an event to JS so listeners can update in real time.
          reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit("batteryLevelChanged", batteryPct)
        }
      }
    }
    // Register the receiver to listen for battery changes.
    reactApplicationContext.registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
  }

  /**
   * Unsubscribes from battery change events.
   */
  override fun unsubscribeFromBatteryChange() {
    batteryReceiver?.let {
      reactApplicationContext.unregisterReceiver(it)
      batteryReceiver = null
    }
  }

  companion object {
    const val NAME = "NativeBattery"
  }
}
