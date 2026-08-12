package com.multitool.app.tools

import java.security.MessageDigest

object TextTools {

    fun base64Encode(text: String): String {
        return android.util.Base64.encodeToString(text.toByteArray(), android.util.Base64.DEFAULT).trim()
    }

    fun base64Decode(text: String): String {
        return try {
            String(android.util.Base64.decode(text, android.util.Base64.DEFAULT))
        } catch (e: Exception) {
            "Ошибка декодирования: ${e.message}"
        }
    }

    data class TextStats(val chars: Int, val words: Int, val lines: Int, val spaces: Int)

    fun countText(text: String): TextStats {
        val trimmed = text.trim()
        val wordCount = if (trimmed.isEmpty()) 0 else trimmed.split(Regex("\\s+")).size
        return TextStats(
            chars = text.length,
            words = wordCount,
            lines = text.lines().size,
            spaces = text.count { it == ' ' }
        )
    }

    fun generatePassword(length: Int = 16, useUpper: Boolean = true, useDigits: Boolean = true, useSymbols: Boolean = true): String {
        val lower = "abcdefghijklmnopqrstuvwxyz"
        val upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val digits = "0123456789"
        val symbs = "!@#%^&*()_+-=[]{}|;:,.<>?"

        var chars = lower
        if (useUpper) chars += upper
        if (useDigits) chars += digits
        if (useSymbols) chars += symbs

        return (1..length).map { chars.random() }.joinToString("")
    }

    fun hash(text: String, algorithm: String = "MD5"): String {
        val digest = MessageDigest.getInstance(algorithm)
        return digest.digest(text.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}
