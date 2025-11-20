package fuck.location.xposed.helpers

import android.util.Log

object SafeLogger {
    private const val TAG = "FuckLocation"

    fun log(message: String) {
        try {
            // Try to use XposedBridge if available (when running as Xposed module)
            val xposedBridgeClass = Class.forName("de.robv.android.xposed.XposedBridge")
            val logMethod = xposedBridgeClass.getMethod("log", String::class.java)
            logMethod.invoke(null, message)
        } catch (e: Exception) {
            // Fallback to Android Log when XposedBridge is not available
            Log.d(TAG, message)
        }
    }
}