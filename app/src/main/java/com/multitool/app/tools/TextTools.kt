package com.multitool.app.tools

import java.security.MessageDigest
import java.util.UUID

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

    fun urlEncode(text: String): String {
        return java.net.URLEncoder.encode(text, "UTF-8")
    }

    fun urlDecode(text: String): String {
        return try {
            java.net.URLDecoder.decode(text, "UTF-8")
        } catch (e: Exception) {
            "Ошибка: ${e.message}"
        }
    }

    data class TextStats(val chars: Int, val words: Int, val lines: Int, val spaces: Int, val digits: Int, val letters: Int)

    fun countText(text: String): TextStats {
        val trimmed = text.trim()
        val wordCount = if (trimmed.isEmpty()) 0 else trimmed.split(Regex("\\s+")).size
        return TextStats(
            chars = text.length,
            words = wordCount,
            lines = text.lines().size,
            spaces = text.count { it == ' ' },
            digits = text.count { it.isDigit() },
            letters = text.count { it.isLetter() }
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

    fun generateUUID(): String = UUID.randomUUID().toString()

    fun generateShortUUID(): String = UUID.randomUUID().toString().take(8)

    fun loremIpsum(count: Int = 50): String {
        val words = listOf(
            "lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit",
            "sed", "do", "eiusmod", "tempor", "incididunt", "ut", "labore", "et", "dolore",
            "magna", "aliqua", "enim", "ad", "minim", "veniam", "quis", "nostrud",
            "exercitation", "ullamco", "laboris", "nisi", "aliquip", "ex", "ea", "commodo",
            "consequat", "duis", "aute", "irure", "dolor", "reprehenderit", "voluptate",
            "velit", "esse", "cillum", "dolore", "eu", "fugiat", "nulla", "pariatur"
        )
        return (1..count).map { words.random() }.joinToString(" ").replaceFirstChar { it.uppercase() } + "."
    }

    fun jsonPrettify(json: String): String {
        return try {
            val indent = 2
            val sb = StringBuilder()
            var level = 0
            var inString = false
            for (char in json) {
                when {
                    char == '"' -> { inString = !inString; sb.append(char) }
                    inString -> sb.append(char)
                    char == '{' || char == '[' -> {
                        sb.append(char)
                        sb.append('\n')
                        level++
                        sb.append(" ".repeat(level * indent))
                    }
                    char == '}' || char == ']' -> {
                        sb.append('\n')
                        level--
                        sb.append(" ".repeat(level * indent))
                        sb.append(char)
                    }
                    char == ',' -> {
                        sb.append(char)
                        sb.append('\n')
                        sb.append(" ".repeat(level * indent))
                    }
                    char == ':' -> sb.append(": ")
                    !char.isWhitespace() -> sb.append(char)
                }
            }
            sb.toString()
        } catch (e: Exception) {
            "Ошибка форматирования: ${e.message}"
        }
    }

    fun rot13(text: String): String {
        return text.map { c ->
            when {
                c in 'a'..'z' -> 'a' + (c - 'a' + 13) % 26
                c in 'A'..'Z' -> 'A' + (c - 'A' + 13) % 26
                else -> c
            }
        }.joinToString("")
    }

    fun reverseText(text: String): String = text.reversed()
}
