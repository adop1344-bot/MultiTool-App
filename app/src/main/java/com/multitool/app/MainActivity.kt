package com.multitool.app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ToolAdapter

    private val tools = listOf(
        // Network
        ToolItem("Ping", "Проверка доступности хоста", R.drawable.ic_network, ToolCategory.NETWORK, "ping"),
        ToolItem("DNS Lookup", "Поиск DNS записей", R.drawable.ic_dns, ToolCategory.NETWORK, "dns"),
        ToolItem("IP Info", "Информация об IP", R.drawable.ic_network, ToolCategory.NETWORK, "ipinfo"),
        ToolItem("Port Check", "Проверка портов", R.drawable.ic_network, ToolCategory.NETWORK, "portcheck"),
        ToolItem("HTTP Check", "HTTP заголовки", R.drawable.ic_network, ToolCategory.NETWORK, "httpcheck"),

        // Text
        ToolItem("Base64 Encode", "Кодирование", R.drawable.ic_text, ToolCategory.TEXT, "b64enc"),
        ToolItem("Base64 Decode", "Декодирование", R.drawable.ic_text, ToolCategory.TEXT, "b64dec"),
        ToolItem("URL Encode", "Кодирование URL", R.drawable.ic_text, ToolCategory.TEXT, "urlenc"),
        ToolItem("URL Decode", "Декодирование URL", R.drawable.ic_text, ToolCategory.TEXT, "urldec"),
        ToolItem("Счётчик", "Символы/слова", R.drawable.ic_counter, ToolCategory.TEXT, "counter"),
        ToolItem("Пароли", "Генератор паролей", R.drawable.ic_password, ToolCategory.TEXT, "password"),
        ToolItem("Хэши", "MD5/SHA1/SHA256", R.drawable.ic_text, ToolCategory.TEXT, "hash"),
        ToolItem("UUID", "Генератор UUID", R.drawable.ic_text, ToolCategory.TEXT, "uuid"),
        ToolItem("Lorem Ipsum", "Генератор текста", R.drawable.ic_text, ToolCategory.TEXT, "lorem"),
        ToolItem("JSON Format", "Форматирование", R.drawable.ic_text, ToolCategory.TEXT, "jsonfmt"),
        ToolItem("ROT13", "Шифр ROT13", R.drawable.ic_text, ToolCategory.TEXT, "rot13"),
        ToolItem("Reverse", "Переворот текста", R.drawable.ic_text, ToolCategory.TEXT, "reverse"),

        // Converters
        ToolItem("Color", "HEX/RGB/HSL", R.drawable.ic_converter, ToolCategory.CONVERTER, "color"),
        ToolItem("Temperature", "Конвертер C/F/K", R.drawable.ic_converter, ToolCategory.CONVERTER, "temp"),
        ToolItem("Length", "Конвертер длины", R.drawable.ic_converter, ToolCategory.CONVERTER, "length"),
        ToolItem("Weight", "Конвертер веса", R.drawable.ic_converter, ToolCategory.CONVERTER, "weight"),
        ToolItem("Data Size", "Конвертер данных", R.drawable.ic_converter, ToolCategory.CONVERTER, "data"),

        // Shizuku
        ToolItem("Shizuku Console", "Выполнить команду", R.drawable.ic_password, ToolCategory.TEXT, "shizuku"),
        ToolItem("Shizuku System", "Инфо о системе", R.drawable.ic_converter, ToolCategory.TEXT, "shizuku_sys"),
        ToolItem("Shizuku Packages", "Список пакетов", R.drawable.ic_text, ToolCategory.TEXT, "shizuku_pkg"),
        ToolItem("Shizuku Processes", "Запущенные процессы", R.drawable.ic_network, ToolCategory.TEXT, "shizuku_proc"),
        ToolItem("Shizuku Storage", "Инфо о хранилище", R.drawable.ic_converter, ToolCategory.TEXT, "shizuku_storage"),
        ToolItem("Shizuku WiFi", "Инфо о WiFi", R.drawable.ic_network, ToolCategory.TEXT, "shizuku_wifi"),
        ToolItem("Shizuku Battery", "Инфо о батарее", R.drawable.ic_converter, ToolCategory.TEXT, "shizuku_bat"),
        ToolItem("Shizuku Cache", "Очистка кэша", R.drawable.ic_counter, ToolCategory.TEXT, "shizuku_cache"),
        ToolItem("Shizuku Props", "Свойства системы", R.drawable.ic_converter, ToolCategory.TEXT, "shizuku_props"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.tools_title)

        recyclerView = findViewById(R.id.recycler_tools)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = ToolAdapter(tools) { tool ->
            openTool(tool)
        }
        recyclerView.adapter = adapter
    }

    private fun openTool(tool: ToolItem) {
        val intent = Intent(this, ToolActivity::class.java)
        intent.putExtra("tool_id", tool.toolId)
        intent.putExtra("tool_name", tool.name)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                val ver = "v${BuildConfig.VERSION_NAME} (build ${BuildConfig.VERSION_CODE})"
                Toast.makeText(this, "MultiTool $ver\n🔥 30+ инструментов", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
