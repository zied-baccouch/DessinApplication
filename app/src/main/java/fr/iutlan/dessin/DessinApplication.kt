package fr.iutlan.dessin

import android.app.Application
import fr.iutlan.dessin.figures.Figure
import java.util.LinkedList

class DessinApplication : Application() {
    var figures: LinkedList<Figure> = LinkedList()
    var typefigure: Figure.Type = Figure.Type.RECTANGLE
    var color: Int = 0xFF000000.toInt() // Noir par défaut
    var isFilled: Boolean = false
    var grid: Boolean = true
}