package com.multitool.app.tools

import android.os.IBinder
import androidx.core.content.ContextCompat
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuProvider

object ShizukuTools {

    data class ShizukuStatus(
        val available: Boolean,
        val version: Int,
        val granted: Boolean
    )

    fun checkStatus(): ShizukuStatus {
        val available = try {
            Shizuku.ping()
            true
        } catch (e: Exception) {
            false
        }
        return ShizukuStatus(
            available = available,
            version = if (available) Shizuku.getVersion() else 0,
            granted = if (available) Shizuku.isGranted(0) else false
        )
    }

    suspend fun runCommand(command: String): String = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        try {
            if (!checkStatus().granted) {
                return@withContext "Shizuku: нет разрешения"
            }
            val result = StringBuilder()
            Shizuku.newProcess(arrayOf("sh", "-c", command), null, null).let { process ->
                process.inputStream.bufferedReader().use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        result.appendLine(line)
                    }
                }
                process.errorStream.bufferedReader().use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        result.appendLine("[ERR] $line")
                    }
                }
                process.waitFor()
                result.appendLine("\n--- Код выхода: ${process.exitValue()} ---")
            }
            result.toString()
        } catch (e: Exception) {
            "Ошибка: ${e.message}"
        }
    }
}
