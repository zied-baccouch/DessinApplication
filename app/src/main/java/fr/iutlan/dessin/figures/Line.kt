package fr.iutlan.dessin.figures

import android.graphics.Canvas
import android.graphics.Paint

class Line(color: Int, isFilled: Boolean) : Figure(color, isFilled) {
    override fun draw(canvas: Canvas?) {
        if (canvas != null && !incomplet()) {
            // Ligne ne nécessite pas de remplissage, car une ligne est toujours un contour
            canvas.drawLine(x1, y1, x2, y2, paint)
        }
    }
}
