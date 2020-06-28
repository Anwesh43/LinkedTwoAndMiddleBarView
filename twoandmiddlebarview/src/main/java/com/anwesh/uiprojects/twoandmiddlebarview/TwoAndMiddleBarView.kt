package com.anwesh.uiprojects.twoandmiddlebarview

/**
 * Created by anweshmishra on 28/06/20.
 */

import android.view.View
import android.view.MotionEvent
import android.content.Context
import android.app.Activity
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.graphics.Paint

val colors : Array<String> = arrayOf("#F44336", "#673AB7", "#4CAF50", "#009688", "#FF5722")
val parts : Int = 3
val delay : Long = 20
val scGap : Float = 0.02f / parts
val h1Factor : Float = 8f
val h2Factor : Float = 4f
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()

fun Canvas.drawTwoAndMiddleBar(scale : Float, w : Float, h : Float, paint : Paint) {
    val h1Bar : Float = h / h1Factor
    val h2Bar : Float = h / h2Factor
    val wBar : Float = w / parts
    val sf : Float = scale.sinify()
    val sf2 : Float = sf.divideScale(1, parts)
    for (j in 0..1) {
        val sfi : Float = sf.divideScale(j * 2, parts)
        save()
        translate(2 * wBar * j, -h1Bar)
        drawRect(RectF(0f, 0f, wBar * sfi, h1Bar), paint)
        restore()
    }
    save()
    translate(wBar, -h2Bar)
    drawRect(RectF(0f, -h2Bar * sf2, wBar, h2Bar * sf2), paint)
    restore()
}

fun Canvas.drawTAMBNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    save()
    translate(0f, h)
    drawTwoAndMiddleBar(scale, w, h, paint)
    restore()
}
