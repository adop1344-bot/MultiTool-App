package com.multitool.app.tools

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rikka.shizuku.Shizuku

object ShizukuTools {

    data class ShizukuStatus(
        val available: Boolean,
        val version: Int,
        val granted: Boolean
    )

    fun checkStatus(): ShizukuStatus {
        val available = try {
            Shizuku.getVersion()
            true
        } catch (e: Exception) {
            false
        }
        val version = if (available) try { Shizuku.getVersion() } catch(e: Exception) { 0 } else 0
        val granted = if (available) try { Shizuku.checkSelfPermission() == 0 } catch(e: Exception) { false } else false
        return ShizukuStatus(available, version, granted)
    }

    suspend fun runCommand(command: String): String = withContext(Dispatchers.IO) {
        try {
            if (!checkStatus().granted) {
                val status = checkStatus()
                if (!status.available) return@withContext "Shizuku не запущен\nУстанови Shizuku из GitHub и запусти"
                return@withContext "Нет разрешения Shizuku\nДай разрешение в приложении Shizuku"
            }
            val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", command))
            val result = StringBuilder()
            process.inputStream.bufferedReader().use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) result.appendLine(line)
            }
            process.errorStream.bufferedReader().use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) result.appendLine("[ERR] $line")
            }
            process.waitFor()
            result.appendLine("\n── Код выхода: ${process.exitValue()} ──")
            result.toString()
        } catch (e: Exception) {
            "Ошибка: ${e.message}"
        }
    }

    // Быстрые команды через Shizuku
    suspend fun getInstalledPackages(): String = runCommand("pm list packages | sort")
    suspend fun getSystemPackages(): String = runCommand("pm list packages -s | sort")
    suspend fun getDeviceInfo(): String = runCommand(
        "echo '📱 УСТРОЙСТВО' && echo 'Модель:' && getprop ro.product.model && " +
        "echo 'Производитель:' && getprop ro.product.manufacturer && " +
        "echo 'Android:' && getprop ro.build.version.release && " +
        "echo 'SDK:' && getprop ro.build.version.sdk && " +
        "echo 'CPU:' && getprop ro.product.cpu.abi && " +
        "echo '' && echo '💾 ПАМЯТЬ' && free -h && " +
        "echo '' && echo '🔋 БАТАРЕЯ' && dumpsys battery | grep -E 'level|status|temperature|technology' && " +
        "echo '' && echo '📡 СЕТЬ' && ip addr show | grep -E 'inet |wlan' | head -5"
    )
    suspend fun getRunningProcesses(): String = runCommand("ps -A | head -30")
    suspend fun getStorageInfo(): String = runCommand("df -h /data /sdcard /system 2>/dev/null")
    suspend fun getWiFiInfo(): String = runCommand("dumpsys wifi | grep -E 'SSID|ipAddress|frequency|rssi' | head -10")
    suspend fun getBatteryStats(): String = runCommand("dumpsys battery")
    suspend fun getAppOps(): String = runCommand("dumpsys appops | head -40")
    suspend fun listDisabledPackages(): String = runCommand("pm list packages -d | sort")
    suspend fun clearCache(): String = runCommand("echo 'Очистка кэша...' && pm trim-caches 10G 2>&1 && echo 'Готово!'")
    suspend fun getBuildProps(): String = runCommand("getprop | grep -E 'build.version|product.model|product.manufacturer|product.cpu|ro.serialno|ro.build.display'")
}
