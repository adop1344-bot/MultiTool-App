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
        ToolItem("Ping", "Проверка доступности хоста", R.drawable.ic_network, ToolCategory.NETWORK, "ping"),
        ToolItem("DNS Lookup", "Поиск DNS записей", R.drawable.ic_dns, ToolCategory.NETWORK, "dns"),
        ToolItem("Base64 Encode", "Кодирование в Base64", R.drawable.ic_text, ToolCategory.TEXT, "b64enc"),
        ToolItem("Base64 Decode", "Декодирование из Base64", R.drawable.ic_text, ToolCategory.TEXT, "b64dec"),
        ToolItem("Счётчик", "Подсчёт символов/слов", R.drawable.ic_counter, ToolCategory.TEXT, "counter"),
        ToolItem("Генератор QR", "Создание QR кода", R.drawable.ic_qr, ToolCategory.CONVERTER, "qr"),
        ToolItem("Конвертер валют", "Курсы валют онлайн", R.drawable.ic_currency, ToolCategory.CONVERTER, "currency"),
        ToolItem("Генератор паролей", "Создание надёжных паролей", R.drawable.ic_password, ToolCategory.TEXT, "password"),
        ToolItem("IP Info", "Информация об IP адресе", R.drawable.ic_network, ToolCategory.NETWORK, "ipinfo"),
        ToolItem("Unit Converter", "Конвертация единиц", R.drawable.ic_converter, ToolCategory.CONVERTER, "units"),
        ToolItem("WHOIS", "Информация о домене", R.drawable.ic_dns, ToolCategory.NETWORK, "whois"),
        ToolItem("Хэширование", "MD5/SHA1/SHA256", R.drawable.ic_text, ToolCategory.TEXT, "hash"),
        // Shizuku tools
        ToolItem("Shizuku Console", "Выполнение команд через Shizuku", R.drawable.ic_password, ToolCategory.TEXT, "shizuku"),
        ToolItem("Shizuku System", "Инфо о системе через Shizuku", R.drawable.ic_converter, ToolCategory.TEXT, "shizuku_sys"),
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
                val ver = "${BuildConfig.VERSION_NAME} (build ${BuildConfig.VERSION_CODE})"
                Toast.makeText(this, "$app_name $ver", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
