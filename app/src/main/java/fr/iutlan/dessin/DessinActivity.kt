package fr.iutlan.dessin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import fr.iutlan.dessin.databinding.DessinActivityBinding
import fr.iutlan.dessin.figures.Figure

class DessinActivity : AppCompatActivity() {

    lateinit var ui: DessinActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mise en place du layout
        ui = DessinActivityBinding.inflate(layoutInflater)
        setContentView(ui.root)
        updateSelections()

        // Écouteurs de la barre d'outils du haut à gauche
        ui.rectangle.setOnClickListener { _ -> ui.dessin.typefigure = Figure.Type.RECTANGLE; updateSelections() }
        ui.circle.setOnClickListener { _ -> ui.dessin.typefigure = Figure.Type.CIRCLE; updateSelections() }
        ui.line.setOnClickListener { _ -> ui.dessin.typefigure = Figure.Type.LINE; updateSelections() }
        ui.ellipse.setOnClickListener { _ -> ui.dessin.typefigure = Figure.Type.ELLIPSE; updateSelections() }

        // Écouteurs de la barre d'outils du bas à gauche
        ui.undo.setOnClickListener { _ -> ui.dessin.undo() }
        ui.undo.setOnLongClickListener { _ -> ui.dessin.clear(); true }
        ui.swap.setOnClickListener { _ -> ui.dessin.swap() }

        // Écouteurs de la barre d'outils du bas à droite
        ui.grid.setOnClickListener { _ -> ui.dessin.grid = !ui.dessin.grid; updateSelections() }
        ui.palette.setOnClickListener(this::onColorSelect)
        ui.save.setOnClickListener(this::onSave)
        ui.fillButton.setOnClickListener {
            ui.dessin.isFilled = !ui.dessin.isFilled
            updateSelections()  // Met à jour l'UI pour refléter l'état du remplissage
        }


        // Nouveau bouton pour inverser l'état du remplissage
        ui.fillButton.setOnClickListener {
            ui.dessin.isFilled = !ui.dessin.isFilled
            updateSelections()  // Met à jour l'UI pour refléter l'état du remplissage
        }
    }

    /** Affiche l'état des sélections : type de figure, grille et remplissage */
    private fun updateSelections() {
        ui.rectangle.setSelected(ui.dessin.typefigure == Figure.Type.RECTANGLE)
        ui.circle.setSelected(ui.dessin.typefigure == Figure.Type.CIRCLE)
        ui.ellipse.setSelected(ui.dessin.typefigure == Figure.Type.ELLIPSE)
        ui.line.setSelected(ui.dessin.typefigure == Figure.Type.LINE)

        ui.grid.setSelected(ui.dessin.grid)
        ui.fillButton.setSelected(ui.dessin.isFilled)  // Met à jour l'état du bouton de remplissage
    }

    /** Affiche un dialogue de sélection de couleur */
    private fun onColorSelect(view: View?) {
        val cpd = ColorPickerDialog(ui.dessin.color) { color ->
            ui.dessin.color = color
            val application = applicationContext as DessinApplication
            application.color = color  // Mise à jour dans l'application
        }
        cpd.show(supportFragmentManager, "colorpickerdlg")
    }

    /** Affiche un dialogue pour enregistrer l'image dans un fichier */
    private fun onSave(view: View?) {
        val intent = Intent()
            .setType("image/png")
            .setAction(Intent.ACTION_CREATE_DOCUMENT)
        chooseFileLauncher.launch(intent)
    }

    var chooseFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let {
                contentResolver.openOutputStream(it)?.use { outputStream ->
                    ui.dessin.save(outputStream)
                }
            }
        }
    }
}
