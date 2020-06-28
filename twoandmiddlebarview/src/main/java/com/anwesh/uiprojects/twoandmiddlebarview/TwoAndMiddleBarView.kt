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
