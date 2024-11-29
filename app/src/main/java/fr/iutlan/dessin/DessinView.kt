package fr.iutlan.dessin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import fr.iutlan.dessin.figures.Figure
import java.io.OutputStream
import java.util.LinkedList
import kotlin.math.round

class DessinView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // Contexte de l'application : mémorise les figures et les préférences
    private val application = context.applicationContext as DessinApplication

    // Liste des figures récupérée directement depuis l'application
    private val figures: LinkedList<Figure> get() = application.figures

    // Type de figure courant (pour la prochaine à être dessinée)
    var typefigure: Figure.Type
        get() = application.typefigure
        set(value) { application.typefigure = value }

    // Couleur courante récupérée depuis l'application
    var color: Int
        get() = application.color
        set(value) { application.color = value }

    // Remplissage des figures
    var isFilled: Boolean
        get() = application.isFilled
        set(value) { application.isFilled = value }

    // Grille activée ou non
    var grid: Boolean
        get() = application.grid
        set(value) {
            application.grid = value
            invalidate()
        }

    // Constantes et configuration de la grille
    val GRID_STRIDE_DP = 24
    private var gridStride: Int = 0
    private val gridPaint = Paint()

    init {
        gridPaint.color = Color.argb(128, 128, 128, 128)
        gridPaint.strokeWidth = 2f
        gridPaint.isAntiAlias = true
        gridPaint.style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        gridStride = (GRID_STRIDE_DP * resources.displayMetrics.density).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Dessiner toutes les figures
        for (figure in figures) {
            figure.draw(canvas)
        }

        // Dessiner la grille si elle est activée
        if (grid) {
            for (x in 0 until width step gridStride) {
                for (y in 0 until height step gridStride) {
                    canvas.drawCircle(x.toFloat(), y.toFloat(), 2f, gridPaint)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var x = event.x
        var y = event.y

        // Ajuster les coordonnées à la grille si activée
        if (grid) {
            x = (round(x / gridStride.toFloat()) * gridStride).toFloat()
            y = (round(y / gridStride.toFloat()) * gridStride).toFloat()
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val figure = Figure.create(typefigure, color, isFilled)
                figure.setP1(x, y)
                figures.add(figure)
            }
            MotionEvent.ACTION_MOVE -> {
                figures.lastOrNull()?.setP2(x, y)
            }
            MotionEvent.ACTION_UP -> performClick()
        }

        invalidate()
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    fun clear() {
        figures.clear()
        invalidate()
    }

    fun undo() {
        if (figures.isNotEmpty()) {
            figures.removeLast()
        }
        invalidate()
    }

    fun swap() {
        if (figures.isNotEmpty()) {
            val firstFigure = figures.removeFirst()
            figures.add(firstFigure)
        }
        invalidate()
    }

    fun save(outputStream: OutputStream?) {
        if (outputStream == null) return
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        this.draw(canvas)
        outputStream.use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
        }
    }
}
