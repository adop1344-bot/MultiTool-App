package com.multitool.app.tools

object ConverterTools {

    data class UnitDef(val name: String, val toBase: Double, val symbol: String)

    // Temperature
    fun convertTemp(value: Double, from: String, to: String): String {
        var celsius = when (from) {
            "C" -> value
            "F" -> (value - 32) * 5 / 9
            "K" -> value - 273.15
            else -> value
        }
        val result = when (to) {
            "C" -> celsius
            "F" -> celsius * 9 / 5 + 32
            "K" -> celsius + 273.15
            else -> celsius
        }
        return "%.2f °${to}".format(result)
    }

    // Length units
    val lengthUnits = listOf(
        UnitDef("mm", 0.001, "мм"),
        UnitDef("cm", 0.01, "см"),
        UnitDef("m", 1.0, "м"),
        UnitDef("km", 1000.0, "км"),
        UnitDef("in", 0.0254, "дюйм"),
        UnitDef("ft", 0.3048, "фут"),
        UnitDef("yd", 0.9144, "ярд"),
        UnitDef("mi", 1609.344, "миля"),
    )

    // Weight units
    val weightUnits = listOf(
        UnitDef("mg", 0.000001, "мг"),
        UnitDef("g", 0.001, "г"),
        UnitDef("kg", 1.0, "кг"),
        UnitDef("t", 1000.0, "т"),
        UnitDef("oz", 0.0283495, "унц"),
        UnitDef("lb", 0.453592, "фунт"),
    )

    // Data units
    val dataUnits = listOf(
        UnitDef("B", 1.0, "B"),
        UnitDef("KB", 1024.0, "KB"),
        UnitDef("MB", 1048576.0, "MB"),
        UnitDef("GB", 1073741824.0, "GB"),
        UnitDef("TB", 1099511627776.0, "TB"),
    )

    fun convert(value: Double, from: UnitDef, to: UnitDef): String {
        val inBase = value * from.toBase
        val result = inBase / to.toBase
        return "%.6f %s".format(result, to.symbol)
    }

    // Color converter
    data class RGB(val r: Int, val g: Int, val b: Int)

    fun hexToRgb(hex: String): RGB? {
        val h = hex.removePrefix("#").take(6)
        if (h.length != 6) return null
        return try {
            RGB(h.substring(0, 2).toInt(16), h.substring(2, 4).toInt(16), h.substring(4, 6).toInt(16))
        } catch (e: Exception) { null }
    }

    fun rgbToHex(r: Int, g: Int, b: Int): String = "#%02X%02X%02X".format(r.coerceIn(0, 255), g.coerceIn(0, 255), b.coerceIn(0, 255))

    fun rgbToHsl(r: Int, g: Int, b: Int): Triple<Double, Double, Double> {
        val rd = r / 255.0; val gd = g / 255.0; val bd = b / 255.0
        val max = maxOf(rd, gd, bd); val min = minOf(rd, gd, bd)
        val l = (max + min) / 2.0
        val s = if (max == min) 0.0 else {
            val d = max - min
            if (l > 0.5) d / (2.0 - max - min) else d / (max + min)
        }
        val h = when {
            max == min -> 0.0
            max == rd -> (60.0 * ((gd - bd) / (max - min)) + 360.0) % 360.0
            max == gd -> 60.0 * ((bd - rd) / (max - min)) + 120.0
            else -> 60.0 * ((rd - gd) / (max - min)) + 240.0
        }
        return Triple(h, s * 100.0, l * 100.0)
    }
}
