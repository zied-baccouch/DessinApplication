package fr.iutlan.dessin.figures

import android.graphics.Canvas

/**
 * Cette classe gère le type de figure rectangle
 * @param color couleur du rectangle
 */
class Rectangle(color: Int, isFilled: Boolean) : Figure(color, isFilled) {
    override fun draw(canvas: Canvas?) {
        if (incomplet()) return
        if (isFilled) {
            canvas?.drawRect(x1, y1, x2, y2, paint)
        } else {
            canvas?.drawRect(x1, y1, x2, y2, paint)
        }
    }
}
