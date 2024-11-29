package fr.iutlan.dessin.figures

import android.graphics.Canvas
import android.graphics.Paint

abstract class Figure(color: Int, val isFilled: Boolean) {

    enum class Type {
        RECTANGLE,
        LINE,
        CIRCLE,
        ELLIPSE
    }

    companion object {
        fun create(type: Type, color: Int, isFilled: Boolean): Figure {
            return when (type) {
                Type.RECTANGLE -> Rectangle(color, isFilled)
                Type.LINE -> Line(color, isFilled)
                Type.CIRCLE -> Circle(color, isFilled)
                Type.ELLIPSE -> Ellipse(color, isFilled)
            }
        }
    }

    protected var x1: Float = 0f
    protected var y1: Float = 0f
    protected var x2: Float = 0f
    protected var y2: Float = 0f
    private var init1 = false
    private var init2 = false

    protected var paint: Paint

    init {
        paint = Paint()
        paint.color = color
        paint.style = if (isFilled) Paint.Style.FILL_AND_STROKE else Paint.Style.STROKE
    }

    protected fun incomplet(): Boolean {
        return !init1 || !init2
    }

    fun setP1(x: Float, y: Float) {
        x1 = x
        y1 = y
        init1 = true
    }

    fun setP2(x: Float, y: Float) {
        x2 = x
        y2 = y
        init2 = true
    }

    abstract fun draw(canvas: Canvas?)
}
