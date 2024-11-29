package fr.iutlan.dessin.figures

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class Ellipse(color: Int, isFilled: Boolean) : Figure(color, isFilled) {
    override fun draw(canvas: Canvas?) {
        if (canvas != null && !incomplet()) {
            // Calcul des coordonnées du rectangle englobant
            val left = kotlin.math.min(x1, x2)
            val top = kotlin.math.min(y1, y2)
            val right = kotlin.math.max(x1, x2)
            val bottom = kotlin.math.max(y1, y2)

            // Création d'un RectF pour dessiner l'ellipse
            val rect = RectF(left, top, right, bottom)

            // Dessin de l'ellipse, avec ou sans remplissage
            if (isFilled) {
                canvas.drawOval(rect, paint)  // Rempli
            } else {
                canvas.drawOval(rect, paint)  // Bordure seule
            }
        }
    }
}
