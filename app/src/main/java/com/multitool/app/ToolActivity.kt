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
                actionBtn.setOnClickListener { exec { NetworkTools.ping(inputField.text.toString().trim()) } }
            }
            "dns" -> {
                inputField.hint = "Домен (google.com)"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "DNS Lookup"
                actionBtn.setOnClickListener { exec { NetworkTools.dnsLookup(inputField.text.toString().trim()) } }
            }
            "ipinfo" -> {
                inputField.hint = "IP (пусто = ваш IP)"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Узнать"
                actionBtn.setOnClickListener { exec { NetworkTools.getIpInfo(inputField.text.toString().trim()) } }
            }
            "portcheck" -> {
                inputField.hint = "Хост (192.168.1.1)"
                inputField2.hint = "Порт (80)"
                actionBtn.text = "Проверить"
                actionBtn.setOnClickListener {
                    val host = inputField.text.toString().trim()
                    val port = inputField2.text.toString().toIntOrNull()
                    if (host.isEmpty() || port == null) { toast("Введите хост и порт"); return@setOnClickListener }
                    exec { NetworkTools.checkPort(host, port) }
                }
            }
            "httpcheck" -> {
                inputField.hint = "URL (google.com)"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Проверить"
                actionBtn.setOnClickListener { exec { NetworkTools.httpCheck(inputField.text.toString().trim()) } }
            }
            // === TEXT ===
            "b64enc" -> { inputField.hint = "Текст"; inputField2.visibility = android.view.View.GONE; actionBtn.text = "Encode"
                actionBtn.setOnClickListener { resultText.text = TextTools.base64Encode(inputField.text.toString()); statusText.text = "Готово" } }
            "b64dec" -> { inputField.hint = "Base64"; inputField2.visibility = android.view.View.GONE; actionBtn.text = "Decode"
                actionBtn.setOnClickListener {
                    val t = inputField.text.toString().trim()
                    if (t.isEmpty()) { toast("Введите Base64"); return@setOnClickListener }
                    resultText.text = TextTools.base64Decode(t); statusText.text = "Готово" } }
            "urlenc" -> { inputField.hint = "Текст"; inputField2.visibility = android.view.View.GONE; actionBtn.text = "URL Encode"
                actionBtn.setOnClickListener { resultText.text = TextTools.urlEncode(inputField.text.toString()); statusText.text = "Готово" } }
            "urldec" -> { inputField.hint = "URL"; inputField2.visibility = android.view.View.GONE; actionBtn.text = "URL Decode"
                actionBtn.setOnClickListener { resultText.text = TextTools.urlDecode(inputField.text.toString().trim()); statusText.text = "Готово" } }
            "counter" -> { inputField.hint = "Текст"; inputField2.visibility = android.view.View.GONE; actionBtn.text = "Счёт"
                actionBtn.setOnClickListener {
                    val s = TextTools.countText(inputField.text.toString())
                    resultText.text = "Символов: ${s.chars}\nСлов: ${s.words}\nСтрок: ${s.lines}\nПробелов: ${s.spaces}\nЦифр: ${s.digits}\nБукв: ${s.letters}"
                    statusText.text = "Готово" } }
            "password" -> { inputField.hint = "Длина (4-64)"; inputField2.visibility = android.view.View.GONE; actionBtn.text = "Сгенерить"
                actionBtn.setOnClickListener {
                    resultText.text = TextTools.generatePassword((inputField.text.toString().toIntOrNull() ?: 16).coerceIn(4, 64))
                    statusText.text = "Готово" } }
            "hash" -> { inputField.hint = "Текст"; inputField2.hint = "Алгоритм"; actionBtn.text = "Хэш"
                actionBtn.setOnClickListener {
                    val t = inputField.text.toString()
                    if (t.isEmpty()) { toast("Введите текст"); return@setOnClickListener }
                    resultText.text = "MD5: ${TextTools.hash(t, "MD5")}\nSHA-1: ${TextTools.hash(t, "SHA-1")}\nSHA-256: ${TextTools.hash(t, "SHA-256")}"
                    statusText.text = "Готово" } }
            "uuid" -> { inputField.visibility = android.view.View.GONE; inputLayout.visibility = android.view.View.GONE
                inputField2.visibility = android.view.View.GONE; inputLayout2.visibility = android.view.View.GONE
                actionBtn.text = "UUID"; actionBtn.setOnClickListener {
                    resultText.text = "UUID: ${TextTools.generateUUID()}\nShort: ${TextTools.generateShortUUID()}"; statusText.text = "Готово" } }
            "lorem" -> { inputField.hint = "Кол-во слов"; inputField2.visibility = android.view.View.GONE; actionBtn.text = "Lorem"
                actionBtn.setOnClickListener {
                    resultText.text = TextTools.loremIpsum((inputField.text.toString().toIntOrNull() ?: 50).coerceIn(5, 500))
                    statusText.text = "Готово" } }
            "jsonfmt" -> { inputField.hint = "JSON"; inputField2.visibility = android.view.View.GONE; inputField.setLines(4); actionBtn.text = "Формат"
                actionBtn.setOnClickListener {
                    val j = inputField.text.toString().trim()
                    if (j.isEmpty()) { toast("Введите JSON"); return@setOnClickListener }
                    resultText.text = TextTools.jsonPrettify(j); statusText.text = "Готово" } }
            "rot13" -> { inputField.hint = "Текст"; inputField2.visibility = android.view.View.GONE; actionBtn.text = "ROT13"
                actionBtn.setOnClickListener { resultText.text = TextTools.rot13(inputField.text.toString()); statusText.text = "Готово" } }
            "reverse" -> { inputField.hint = "Текст"; inputField2.visibility = android.view.View.GONE; actionBtn.text = "Реверс"
                actionBtn.setOnClickListener { resultText.text = TextTools.reverseText(inputField.text.toString()); statusText.text = "Готово" } }
            // === CONVERTERS ===
            "color" -> { inputField.hint = "HEX (#FF0000)"; inputField2.visibility = android.view.View.GONE; actionBtn.text = "Конверт"
                actionBtn.setOnClickListener {
                    val rgb = ConverterTools.hexToRgb(inputField.text.toString().trim().removePrefix("#"))
                    if (rgb != null) { val h = ConverterTools.rgbToHsl(rgb.r, rgb.g, rgb.b)
                        resultText.text = "HEX: ${ConverterTools.rgbToHex(rgb.r, rgb.g, rgb.b)}\nRGB: ${rgb.r}, ${rgb.g}, ${rgb.b}\nHSL: ${"%.1f".format(h.first)}°, ${"%.1f".format(h.second)}%, ${"%.1f".format(h.third)}%"
                    } else resultText.text = "Неверный формат"
                    statusText.text = "Готово" } }
            "temp" -> { inputField.hint = "Значение (25)"; inputField2.hint = "Из->В (C->F)"; actionBtn.text = "Конверт"
                actionBtn.setOnClickListener {
                    val v = inputField.text.toString().toDoubleOrNull()
                    val p = inputField2.text.toString().trim().split("->", ">", "→", " ")
                    if (v == null || p.size < 2) { toast("Формат: 25 C->F"); return@setOnClickListener }
                    resultText.text = ConverterTools.convertTemp(v, p[0].trim().uppercase().take(1), p[1].trim().uppercase().take(1))
                    statusText.text = "Готово" } }
            "length" -> unitConv(ConverterTools.lengthUnits, "м->см")
            "weight" -> unitConv(ConverterTools.weightUnits, "кг->г")
            "data" -> unitConv(ConverterTools.dataUnits, "MB->GB")
            // === SHIZUKU ===
            "shizuku" -> {
                inputField.hint = "Команда (pm list packages)"
                inputField2.visibility = android.view.View.GONE
                actionBtn.text = "Выполнить"
                actionBtn.setOnClickListener {
                    val cmd = inputField.text.toString().trim()
                    if (cmd.isEmpty()) { toast("Введите команду"); return@setOnClickListener }
                    exec { ShizukuTools.runCommand(cmd) }
                }
                updateShizukuStatus()
            }
            "shizuku_sys" -> shizukuQuick("Инфо о системе") { ShizukuTools.getDeviceInfo() }
            "shizuku_pkg" -> shizukuQuick("Список пакетов") { ShizukuTools.getInstalledPackages() }
            "shizuku_proc" -> shizukuQuick("Процессы") { ShizukuTools.getRunningProcesses() }
            "shizuku_storage" -> shizukuQuick("Хранилище") { ShizukuTools.getStorageInfo() }
            "shizuku_wifi" -> shizukuQuick("WiFi") { ShizukuTools.getWiFiInfo() }
            "shizuku_bat" -> shizukuQuick("Батарея") { ShizukuTools.getBatteryStats() }
            "shizuku_cache" -> shizukuQuick("Очистка кэша") { ShizukuTools.clearCache() }
            "shizuku_props" -> shizukuQuick("Свойства") { ShizukuTools.getBuildProps() }
        }
    }

    private fun unitConv(units: List<ConverterTools.UnitDef>, example: String) {
        inputField.hint = "Значение"; inputField2.hint = "Из->В ($example)"
        actionBtn.text = "Конверт"
        actionBtn.setOnClickListener {
            val v = inputField.text.toString().toDoubleOrNull()
            val p = inputField2.text.toString().trim().split("->", ">", "→", " ")
            if (v == null || p.size < 2) { toast("Формат: 100 m->cm"); return@setOnClickListener }
            val from = units.find { it.symbol == p[0].trim() || it.name == p[0].trim() }
            val to = units.find { it.symbol == p[1].trim() || it.name == p[1].trim() }
            if (from == null || to == null) { resultText.text = "Неверные единицы"; return@setOnClickListener }
            resultText.text = ConverterTools.convert(v, from, to)
            statusText.text = "Готово"
        }
    }

    private fun shizukuQuick(label: String, block: suspend () -> String) {
        inputField.visibility = android.view.View.GONE; inputLayout.visibility = android.view.View.GONE
        inputField2.visibility = android.view.View.GONE; inputLayout2.visibility = android.view.View.GONE
        actionBtn.text = label
        actionBtn.setOnClickListener { exec(block) }
        updateShizukuStatus()
    }

    private fun updateShizukuStatus() {
        val st = ShizukuTools.checkStatus()
        statusText.text = if (st.available) "Shizuku v${st.version} ${if(st.granted) "✅" else "⚠️ нет прав"}" else "❌ Shizuku не запущен"
    }

    private fun exec(block: suspend () -> String) {
        statusText.text = "Выполняю..."
        resultText.text = ""
        CoroutineScope(Dispatchers.Main).launch {
            resultText.text = block()
            statusText.text = "Готово"
        }
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    override fun onSupportNavigateUp(): Boolean { onBackPressed(); return true }
}
