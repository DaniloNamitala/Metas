package dev.namitala.metas.extensions
import android.graphics.Color
import kotlin.math.roundToInt

fun Int.colorMultiply(factor : Float) : Int {
    val a = Color.alpha(this)
    val r = (Color.red(this) * factor).roundToInt()
    val g = (Color.green(this) * factor).roundToInt()
    val b = (Color.blue(this) * factor).roundToInt()
    return Color.argb(a,
        r.coerceAtMost(255),
        g.coerceAtMost(255),
        b.coerceAtMost(255)
    )
}

fun Int.isDarkColor() : Boolean {
    val a = Color.alpha(this)
    val r = Color.red(this)
    val g = Color.green(this)
    val b = Color.blue(this)
    val luma = 0.2126 * r + 0.7152 * g + 0.0722 * b
    return luma < 40
}