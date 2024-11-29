package fr.iutlan.dessin.figures

import android.graphics.Canvas
import android.graphics.Paint

class Circle(color: Int, isFilled: Boolean) : Figure(color, isFilled) {
    override fun draw(canvas: Canvas?) {
        if (canvas != null && !incomplet()) {
            // Calcul du centre
            val xc = (x1 + x2) / 2
            val yc = (y1 + y2) / 2

            // Calcul du rayon comme distance euclidienne
            val dx = x2 - xc
            val dy = y2 - yc
            val radius = kotlin.math.sqrt(dx * dx + dy * dy)

            // Dessin du cercle, avec ou sans remplissage
            if (isFilled) {
                canvas.drawCircle(xc, yc, radius, paint)  // Rempli
            } else {
                canvas.drawCircle(xc, yc, radius, paint)  // Bordure seule
            }
        }
    }
}
