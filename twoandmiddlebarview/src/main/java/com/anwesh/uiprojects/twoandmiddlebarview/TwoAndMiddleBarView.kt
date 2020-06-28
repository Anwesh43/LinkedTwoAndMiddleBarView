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

class TwoAndMiddleBarView(ctx : Context) : View(ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class TAMBNode(var i : Int, val state : State = State()) {

        private var next : TAMBNode? = null
        private var prev : TAMBNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = TAMBNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawTAMBNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun  getNext(dir : Int, cb : () -> Unit) : TAMBNode {
            var curr : TAMBNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class TwoAndMiddleBar(var i : Int) {

        private var curr : TAMBNode = TAMBNode(0)
        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(cb : (Float) -> Unit) {
            curr.update {
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }

    data class Renderer(var view : TwoAndMiddleBarView) {

        private val animator : Animator = Animator(view)
        private val tamb : TwoAndMiddleBar = TwoAndMiddleBar(0)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            tamb.draw(canvas, paint)
            animator.animate {
                tamb.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            tamb.startUpdating {
                animator.start()
            }
        }
    }

    companion object {

        fun create(activity : Activity) : TwoAndMiddleBarView {
            val view : TwoAndMiddleBarView = TwoAndMiddleBarView(activity)
            activity.setContentView(view)
            return view
        }
    }
}