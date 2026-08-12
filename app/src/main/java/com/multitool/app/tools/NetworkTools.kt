package com.multitool.app.tools

import java.net.InetAddress
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

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
            val queryIp = if (ip.isEmpty()) "" else "/$ip"
            val url = URL("http://ip-api.com/json$queryIp")
            val text = url.readText()
            JSONObject(text).let { json ->
                buildString {
                    appendLine("IP: ${json.getString("query")}")
                    appendLine("Страна: ${json.getString("country")}")
                    appendLine("Регион: ${json.getString("regionName")}")
                    appendLine("Город: ${json.getString("city")}")
                    appendLine("Провайдер: ${json.getString("isp")}")
                    appendLine("Орг: ${json.getString("org")}")
                    appendLine("Широта: ${json.getDouble("lat")}")
                    appendLine("Долгота: ${json.getDouble("lon")}")
                }
            }
        } catch (e: Exception) {
            "Ошибка: ${e.message}"
        }
    }

    suspend fun checkPort(host: String, port: Int): String = withContext(Dispatchers.IO) {
        try {
            val socket = java.net.Socket()
            socket.connect(java.net.InetSocketAddress(host, port), 3000)
            socket.close()
            "Порт $port ($host) — ОТКРЫТ"
        } catch (e: Exception) {
            "Порт $port ($host) — ЗАКРЫТ (${e.message})"
        }
    }

    suspend fun httpCheck(url: String): String = withContext(Dispatchers.IO) {
        try {
            val u = if (!url.startsWith("http")) "https://$url" else url
            val conn = URL(u).openConnection() as java.net.HttpURLConnection
            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            conn.instanceFollowRedirects = false
            conn.connect()
            buildString {
                appendLine("URL: $u")
                appendLine("Код: ${conn.responseCode}")
                appendLine("Сообщение: ${conn.responseMessage}")
                appendLine("Тип: ${conn.contentType ?: "N/A"}")
                appendLine("Длина: ${conn.contentLengthLong}")
                appendLine("Сервер: ${conn.getHeaderField("Server") ?: "N/A"}")
            }
        } catch (e: Exception) {
            "Ошибка: ${e.message}"
        }
    }
}
