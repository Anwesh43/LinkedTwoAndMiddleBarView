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
