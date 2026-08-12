package com.multitool.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.multitool.app.tools.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ToolAdapter

    private val tools = listOf(
        ToolItem("Ping", "Проверка доступности хоста", R.drawable.ic_network, ToolCategory.NETWORK),
        ToolItem("DNS Lookup", "Поиск DNS записей", R.drawable.ic_dns, ToolCategory.NETWORK),
        ToolItem("Base64 Encode", "Кодирование в Base64", R.drawable.ic_text, ToolCategory.TEXT),
        ToolItem("Base64 Decode", "Декодирование из Base64", R.drawable.ic_text, ToolCategory.TEXT),
        ToolItem("Счётчик", "Подсчёт символов/слов", R.drawable.ic_counter, ToolCategory.TEXT),
        ToolItem("Генератор QR", "Создание QR кода", R.drawable.ic_qr, ToolCategory.CONVERTER),
        ToolItem("Конвертер валют", "Курсы валют онлайн", R.drawable.ic_currency, ToolCategory.CONVERTER),
        ToolItem("Генератор паролей", "Создание надёжных паролей", R.drawable.ic_password, ToolCategory.TEXT),
        ToolItem("IP Info", "Информация об IP адресе", R.drawable.ic_network, ToolCategory.NETWORK),
        ToolItem("Unit Converter", "Конвертация единиц", R.drawable.ic_converter, ToolCategory.CONVERTER),
        ToolItem("WHOIS", "Информация о домене", R.drawable.ic_dns, ToolCategory.NETWORK),
        ToolItem("Хэширование", "MD5/SHA1/SHA256", R.drawable.ic_text, ToolCategory.TEXT),
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
        Toast.makeText(this, "${tool.name} — в разработке", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                Toast.makeText(this, "${getString(R.string.app_name)} ${getString(R.string.version)}", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
