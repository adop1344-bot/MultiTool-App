package com.multitool.app

import android.content.ClipData
import android.content.ClipboardManager
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
    private lateinit var inputLayout: com.google.android.material.textfield.TextInputLayout
    private lateinit var inputLayout2: com.google.android.material.textfield.TextInputLayout
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
        inputLayout = findViewById(R.id.inputLayout)
        inputLayout2 = findViewById(R.id.inputLayout2)
        resultText = findViewById(R.id.resultText)
        actionBtn = findViewById(R.id.actionBtn)
        copyBtn = findViewById(R.id.copyBtn)

        setupTool()

        copyBtn.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("result", resultText.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Скопировано!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupTool() {
        when (toolId) {
            // === NETWORK ===
            "ping" -> {
                inputField.hint = "Хост (google.com)"
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
                inputField.hint = "Домен (google.com)"
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
            "ipinfo" -> {
                inputField.hint = "IP (пусто = ваш IP)"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Узнать"
                actionBtn.setOnClickListener {
                    val ip = inputField.text.toString().trim()
                    statusText.text = "Получаю..."
                    resultText.text = ""
                    CoroutineScope(Dispatchers.Main).launch {
                        resultText.text = NetworkTools.getIpInfo(ip)
                        statusText.text = "Готово"
                    }
                }
            }
            "portcheck" -> {
                inputField.hint = "Хост (192.168.1.1)"
                inputField2.hint = "Порт (80)"
                actionBtn.text = "Проверить"
                actionBtn.setOnClickListener {
                    val host = inputField.text.toString().trim()
                    val port = inputField2.text.toString().toIntOrNull()
                    if (host.isEmpty() || port == null) { Toast.makeText(this, "Введите хост и порт", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
                    statusText.text = "Проверяю..."
                    resultText.text = ""
                    CoroutineScope(Dispatchers.Main).launch {
                        resultText.text = NetworkTools.checkPort(host, port)
                        statusText.text = "Готово"
                    }
                }
            }
            "httpcheck" -> {
                inputField.hint = "URL (google.com)"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Проверить"
                actionBtn.setOnClickListener {
                    val url = inputField.text.toString().trim()
                    if (url.isEmpty()) { Toast.makeText(this, "Введите URL", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
                    statusText.text = "Проверяю..."
                    resultText.text = ""
                    CoroutineScope(Dispatchers.Main).launch {
                        resultText.text = NetworkTools.httpCheck(url)
                        statusText.text = "Готово"
                    }
                }
            }
            // === TEXT ===
            "b64enc" -> {
                inputField.hint = "Текст для кодирования"
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
                inputField.hint = "Base64 строка"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Decode"
                actionBtn.setOnClickListener {
                    val text = inputField.text.toString().trim()
                    if (text.isEmpty()) { Toast.makeText(this, "Введите Base64", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
                    resultText.text = TextTools.base64Decode(text)
                    statusText.text = "Готово"
                }
            }
            "urlenc" -> {
                inputField.hint = "Текст для кодирования"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "URL Encode"
                actionBtn.setOnClickListener {
                    resultText.text = TextTools.urlEncode(inputField.text.toString())
                    statusText.text = "Готово"
                }
            }
            "urldec" -> {
                inputField.hint = "URL для декодирования"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "URL Decode"
                actionBtn.setOnClickListener {
                    resultText.text = TextTools.urlDecode(inputField.text.toString().trim())
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
                        Цифр: ${stats.digits}
                        Букв: ${stats.letters}
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
            "hash" -> {
                inputField.hint = "Введите текст"
                inputField2.hint = "Алгоритм (MD5/SHA-1/SHA-256)"
                actionBtn.text = "Хэшировать"
                actionBtn.setOnClickListener {
                    val text = inputField.text.toString()
                    if (text.isEmpty()) { Toast.makeText(this, "Введите текст", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
                    resultText.text = """
                        MD5: ${TextTools.hash(text, "MD5")}
                        SHA-1: ${TextTools.hash(text, "SHA-1")}
                        SHA-256: ${TextTools.hash(text, "SHA-256")}
                    """.trimIndent()
                    statusText.text = "Готово"
                }
            }
            "uuid" -> {
                inputField.visibility = android.view.View.GONE
                inputLayout.visibility = android.view.View.GONE
                inputField2.visibility = android.view.View.GONE
                inputLayout2.visibility = android.view.View.GONE
                actionBtn.text = "Сгенерировать UUID"
                actionBtn.setOnClickListener {
                    resultText.text = """
                        UUID: ${TextTools.generateUUID()}
                        Short: ${TextTools.generateShortUUID()}
                    """.trimIndent()
                    statusText.text = "Готово"
                }
            }
            "lorem" -> {
                inputField.hint = "Количество слов (50)"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Сгенерировать"
                actionBtn.setOnClickListener {
                    val count = inputField.text.toString().toIntOrNull() ?: 50
                    resultText.text = TextTools.loremIpsum(count.coerceIn(5, 500))
                    statusText.text = "Готово"
                }
            }
            "jsonfmt" -> {
                inputField.hint = "Введите JSON строку"
                inputField2.visibility = android.view.View.GONE
                inputField.setLines(5)
                actionBtn.text = "Форматировать"
                actionBtn.setOnClickListener {
                    val json = inputField.text.toString().trim()
                    if (json.isEmpty()) { Toast.makeText(this, "Введите JSON", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
                    resultText.text = TextTools.jsonPrettify(json)
                    statusText.text = "Готово"
                }
            }
            "rot13" -> {
                inputField.hint = "Текст для ROT13"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Преобразовать"
                actionBtn.setOnClickListener {
                    resultText.text = TextTools.rot13(inputField.text.toString())
                    statusText.text = "Готово"
                }
            }
            "reverse" -> {
                inputField.hint = "Текст для переворота"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Перевернуть"
                actionBtn.setOnClickListener {
                    resultText.text = TextTools.reverseText(inputField.text.toString())
                    statusText.text = "Готово"
                }
            }
            // === CONVERTERS ===
            "color" -> {
                inputField.hint = "HEX (#FF0000) или RGB (255,0,0)"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Конвертировать"
                actionBtn.setOnClickListener {
                    val text = inputField.text.toString().trim()
                    if (text.isEmpty()) { Toast.makeText(this, "Введите цвет", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
                    val rgb = ConverterTools.hexToRgb(text.removePrefix("#"))
                    if (rgb != null) {
                        val hsl = ConverterTools.rgbToHsl(rgb.r, rgb.g, rgb.b)
                        resultText.text = """
                            HEX: ${ConverterTools.rgbToHex(rgb.r, rgb.g, rgb.b)}
                            RGB: ${rgb.r}, ${rgb.g}, ${rgb.b}
                            HSL: ${"%.1f".format(hsl.first)}°, ${"%.1f".format(hsl.second)}%, ${"%.1f".format(hsl.third)}%
                        """.trimIndent()
                        statusText.text = "Готово"
                    } else {
                        resultText.text = "Неверный формат цвета"
                    }
                }
            }
            "temp" -> {
                inputField.hint = "Значение (25)"
                inputField2.hint = "Из (C/F/K) -> В (C/F/K)"
                actionBtn.text = "Конвертировать"
                actionBtn.setOnClickListener {
                    val value = inputField.text.toString().toDoubleOrNull()
                    val parts = inputField2.text.toString().trim().split("->", ">", " ")
                    if (value == null || parts.size < 2) {
                        Toast.makeText(this, "Введите: значение и C->F", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    resultText.text = ConverterTools.convertTemp(value, parts[0].trim().uppercase().take(1), parts[1].trim().uppercase().take(1))
                    statusText.text = "Готово"
                }
            }
            "length", "weight", "data" -> {
                val units = when (toolId) {
                    "length" -> ConverterTools.lengthUnits
                    "weight" -> ConverterTools.weightUnits
                    else -> ConverterTools.dataUnits
                }
                val unitNames = units.joinToString(", ") { it.symbol }
                inputField.hint = "Значение"
                inputField2.hint = "Из -> В (например: m->cm)"
                actionBtn.text = "Конвертировать"
                actionBtn.setOnClickListener {
                    val value = inputField.text.toString().toDoubleOrNull()
                    val parts = inputField2.text.toString().trim().split("->", ">", " ")
                    if (value == null || parts.size < 2) {
                        Toast.makeText(this, "Формат: значение из->в", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    val from = units.find { it.symbol == parts[0].trim() || it.name == parts[0].trim() }
                    val to = units.find { it.symbol == parts[1].trim() || it.name == parts[1].trim() }
                    if (from == null || to == null) {
                        resultText.text = "Неверные единицы. Доступны: $unitNames"
                        return@setOnClickListener
                    }
                    resultText.text = ConverterTools.convert(value, from, to)
                    statusText.text = "Готово"
                }
            }
            // === SHIZUKU ===
            "shizuku" -> {
                inputField.hint = "Команда (pm list packages)"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Выполнить"
                ShizukuTools.checkStatus().let { st ->
                    statusText.text = if (st.available) "Shizuku v${st.version} ${if(st.granted) "✓" else "✗ нет прав"}" else "Shizuku не запущен"
                }
                actionBtn.setOnClickListener {
                    val cmd = inputField.text.toString().trim()
                    if (cmd.isEmpty()) { Toast.makeText(this, "Введите команду", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
                    val st = ShizukuTools.checkStatus()
                    if (!st.granted) { Toast.makeText(this, "Нет разрешения Shizuku", Toast.LENGTH_LONG).show(); return@setOnClickListener }
                    statusText.text = "Выполняю..."
                    resultText.text = ""
                    CoroutineScope(Dispatchers.Main).launch {
                        resultText.text = ShizukuTools.runCommand(cmd)
                        statusText.text = "Готово"
                    }
                }
            }
            "shizuku_sys" -> {
                inputField.visibility = android.view.View.GONE
                inputLayout.visibility = android.view.View.GONE
                inputField2.visibility = android.view.View.GONE
                inputLayout2.visibility = android.view.View.GONE
                actionBtn.text = "Инфо о системе"
                actionBtn.setOnClickListener {
                    val st = ShizukuTools.checkStatus()
                    if (!st.granted) { Toast.makeText(this, "Нет разрешения Shizuku", Toast.LENGTH_LONG).show(); return@setOnClickListener }
                    statusText.text = "Собираю..."
                    resultText.text = ""
                    CoroutineScope(Dispatchers.Main).launch {
                        resultText.text = ShizukuTools.runCommand(
                            "echo '=== Устройство ===' && uname -a && echo '' && " +
                            "echo '=== CPU ===' && cat /proc/cpuinfo | grep -E 'model name|Hardware|Processor' | head -5 && echo '' && " +
                            "echo '=== Память ===' && free -h && echo '' && " +
                            "echo '=== Разделы ===' && df -h /data /system 2>/dev/null && echo '' && " +
                            "echo '=== Android ===' && getprop ro.build.version.release && getprop ro.build.version.sdk"
                        )
                        statusText.text = "Готово"
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
