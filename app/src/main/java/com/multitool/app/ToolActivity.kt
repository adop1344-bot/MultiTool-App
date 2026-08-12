package com.multitool.app

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.multitool.app.tools.*
import kotlinx.coroutines.*

class ToolActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var inputField: EditText
    private lateinit var inputField2: EditText
    private lateinit var resultText: TextView
    private lateinit var actionBtn: Button
    private lateinit var copyBtn: Button
    private var toolId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tool)

        toolId = intent.getStringExtra("tool_id") ?: ""
        val toolName = intent.getStringExtra("tool_name") ?: "Инструмент"

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = toolName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        statusText = findViewById(R.id.statusText)
        inputField = findViewById(R.id.inputField)
        inputField2 = findViewById(R.id.inputField2)
        resultText = findViewById(R.id.resultText)
        actionBtn = findViewById(R.id.actionBtn)
        copyBtn = findViewById(R.id.copyBtn)

        setupTool()

        copyBtn.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("result", resultText.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Скопировано!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupTool() {
        when (toolId) {
            "ping" -> {
                inputField.hint = "Введите хост (например, google.com)"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Ping"
                actionBtn.setOnClickListener {
                    val host = inputField.text.toString().trim()
                    if (host.isEmpty()) { Toast.makeText(this, "Введите хост", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
                    statusText.text = "Пингую $host..."
                    resultText.text = ""
                    CoroutineScope(Dispatchers.Main).launch {
                        resultText.text = NetworkTools.ping(host)
                        statusText.text = "Готово"
                    }
                }
            }
            "dns" -> {
                inputField.hint = "Введите домен (например, google.com)"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "DNS Lookup"
                actionBtn.setOnClickListener {
                    val host = inputField.text.toString().trim()
                    if (host.isEmpty()) { Toast.makeText(this, "Введите домен", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
                    statusText.text = "Ищу DNS..."
                    resultText.text = ""
                    CoroutineScope(Dispatchers.Main).launch {
                        resultText.text = NetworkTools.dnsLookup(host)
                        statusText.text = "Готово"
                    }
                }
            }
            "b64enc" -> {
                inputField.hint = "Введите текст для кодирования"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Encode"
                actionBtn.setOnClickListener {
                    val text = inputField.text.toString()
                    if (text.isEmpty()) { Toast.makeText(this, "Введите текст", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
                    resultText.text = TextTools.base64Encode(text)
                    statusText.text = "Готово"
                }
            }
            "b64dec" -> {
                inputField.hint = "Введите Base64 строку"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Decode"
                actionBtn.setOnClickListener {
                    val text = inputField.text.toString().trim()
                    if (text.isEmpty()) { Toast.makeText(this, "Введите Base64", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
                    resultText.text = TextTools.base64Decode(text)
                    statusText.text = "Готово"
                }
            }
            "counter" -> {
                inputField.hint = "Введите текст"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Посчитать"
                actionBtn.setOnClickListener {
                    val text = inputField.text.toString()
                    val stats = TextTools.countText(text)
                    resultText.text = """
                        Символов: ${stats.chars}
                        Слов: ${stats.words}
                        Строк: ${stats.lines}
                        Пробелов: ${stats.spaces}
                    """.trimIndent()
                    statusText.text = "Готово"
                }
            }
            "password" -> {
                inputField.hint = "Длина пароля (по умолч. 16)"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Сгенерировать"
                actionBtn.setOnClickListener {
                    val len = inputField.text.toString().toIntOrNull() ?: 16
                    resultText.text = TextTools.generatePassword(len.coerceIn(4, 64))
                    statusText.text = "Готово"
                }
            }
            "ipinfo" -> {
                inputField.hint = "Введите IP (или оставьте пустым для своего)"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Узнать IP"
                actionBtn.setOnClickListener {
                    val ip = inputField.text.toString().trim()
                    statusText.text = "Получаю информацию..."
                    resultText.text = ""
                    CoroutineScope(Dispatchers.Main).launch {
                        resultText.text = NetworkTools.getIpInfo(if (ip.isEmpty()) "" else ip)
                        statusText.text = "Готово"
                    }
                }
            }
            "hash" -> {
                inputField.hint = "Введите текст"
                inputField2.hint = "Алгоритм (MD5/SHA-1/SHA-256)"
                actionBtn.text = "Хэшировать"
                actionBtn.setOnClickListener {
                    val text = inputField.text.toString()
                    if (text.isEmpty()) { Toast.makeText(this, "Введите текст", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
                    val algo = when (inputField2.text.toString().trim().uppercase()) {
                        "SHA1", "SHA-1" -> "SHA-1"
                        "SHA256", "SHA-256" -> "SHA-256"
                        else -> "MD5"
                    }
                    resultText.text = """
                        MD5: ${TextTools.hash(text, "MD5")}
                        SHA-1: ${TextTools.hash(text, "SHA-1")}
                        SHA-256: ${TextTools.hash(text, "SHA-256")}
                    """.trimIndent()
                    statusText.text = "Готово"
                }
            }
            "qr" -> {
                inputField.hint = "Введите текст или URL"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Создать QR"
                actionBtn.setOnClickListener {
                    Toast.makeText(this, "QR генератор будет добавлен в следующей версии", Toast.LENGTH_SHORT).show()
                }
            }
            "currency" -> {
                inputField.hint = "Сумма"
                inputField2.hint = "USD -> RUB"
                actionBtn.text = "Конвертировать"
                actionBtn.setOnClickListener {
                    Toast.makeText(this, "Конвертер валют будет добавлен в следующей версии", Toast.LENGTH_SHORT).show()
                }
            }
            "whois" -> {
                inputField.hint = "Введите домен"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "WHOIS"
                actionBtn.setOnClickListener {
                    Toast.makeText(this, "WHOIS будет добавлен в следующей версии", Toast.LENGTH_SHORT).show()
                }
            }
            "units" -> {
                inputField.hint = "Введите значение"
                inputField2.hint = "Единицы (cm->m, kg->g)"
                actionBtn.text = "Конвертировать"
                actionBtn.setOnClickListener {
                    Toast.makeText(this, "Unit Converter будет добавлен в следующей версии", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
