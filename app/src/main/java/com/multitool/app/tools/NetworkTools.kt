package com.multitool.app.tools

import java.net.InetAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object NetworkTools {

    suspend fun ping(host: String, count: Int = 4): String = withContext(Dispatchers.IO) {
        try {
            val result = StringBuilder()
            for (i in 1..count) {
                val start = System.currentTimeMillis()
                val address = InetAddress.getByName(host)
                val reachable = address.isReachable(5000)
                val time = System.currentTimeMillis() - start
                result.appendLine("Ответ от ${address.hostAddress}: время=${time}мс ${if (reachable) "OK" else "потерян"}")
            }
            result.toString()
        } catch (e: Exception) {
            "Ошибка: ${e.message}"
        }
    }

    suspend fun dnsLookup(host: String): String = withContext(Dispatchers.IO) {
        try {
            val addresses = InetAddress.getAllByName(host)
            addresses.joinToString("\n") { "${it.hostName} -> ${it.hostAddress}" }
        } catch (e: Exception) {
            "Не удалось найти: ${e.message}"
        }
    }

    suspend fun getIpInfo(ip: String): String = withContext(Dispatchers.IO) {
        try {
            val url = java.net.URL("http://ip-api.com/json/$ip")
            val text = url.readText()
            org.json.JSONObject(text).let { json ->
                buildString {
                    appendLine("IP: ${json.getString("query")}")
                    appendLine("Страна: ${json.getString("country")}")
                    appendLine("Регион: ${json.getString("regionName")}")
                    appendLine("Город: ${json.getString("city")}")
                    appendLine("Провайдер: ${json.getString("isp")}")
                    appendLine("Орг: ${json.getString("org")}")
                }
            }
        } catch (e: Exception) {
            "Ошибка: ${e.message}"
        }
    }
}
