package fr.iutlan.dessin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.DialogFragment

import fr.iutlan.dessin.databinding.ColorPickerViewBinding


/**
 * Cette classe affiche un dialogue pour choisir une couleur.
 * Il faut fournir la couleur initiale en premier paramètre.
 * Il faut fournir un écouteur (color: Int) -> Unit qui sera appelé
 * à la fermeture du dialogue, avec la couleur choisie en paramètre.
 *
 * NB: on utilise le constructeur pour spécifier des paramètres (couleur initiale
 * et écouteur) mais ce n'est plus une bonne manière de faire (deprecated).
 * Il faut normalement définir un Intent et passer les paramètres en extra.
 * C'est parce que le fragment a son propre cycle de vie et peut être
 * reconstruit à tout moment par Android, par exemple si on bascule l'écran
 * et dans ce cas les paramètres seraient perdus.
 * La bonne solution est trop compliquée à mettre en œuvre pour ce TP.
 * Voir https://stackoverflow.com/questions/46587222/passing-a-listener-to-a-custom-fragment-in-android
 * cependant même les solutions proposées ne sont pas entièrement satisfaisantes.
 *
 * @param initialColor : couleur initiale affichée dans le sélecteur
 * @param listener : un écouteur à appeler quand la couleur est validée par l'utilisateur, son paramètre est la couleur validée
 */
class ColorPickerDialog(private val initialColor: Int = Color.BLUE, val listener: (color: Int) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val cpv = ColorPickerView(activity, initialColor)
        return AlertDialog.Builder(activity)
            .setTitle(R.string.choose_color_title)
            .setView(cpv)
            .setPositiveButton(android.R.string.ok) { dialog, idbtn ->
                // appeler l'écouteur avec la couleur actuelle
                listener(cpv.color)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

    @SuppressLint("ViewConstructor")
    class ColorPickerView(context: Context?, var color: Int) : LinearLayout(context) {
        init {
            // mettre en place le layout avec un ViewBinding
            val ui = ColorPickerViewBinding.inflate(LayoutInflater.from(context), this, true)

            // mettre la couleur initiale dans l'échantillon de couleur
            ui.ivColorSample.setBackgroundColor(color)

            // initialiser les curseurs à la valeur de la composante correspondante
            ui.sbRed.progress = Color.red(color)
            ui.sbGreen.progress = Color.green(color)  // Initialisation du composant vert
            ui.sbBlue.progress = Color.blue(color)    // Initialisation du composant bleu
            ui.sbAlpha.progress = Color.alpha(color)  // Initialisation du composant alpha

            // écouteur quand on change l'un des curseurs
            val listener = object : OnSeekBarChangeListener {
                // appelée quand le SeekBar est modifié
                override fun onProgressChanged(seekBar: SeekBar, value: Int, fromUser: Boolean) {
                    // on reconstruit la couleur avec les 4 curseurs
                    color = Color.argb(
                        ui.sbAlpha.progress,   // alpha
                        ui.sbRed.progress,     // rouge
                        ui.sbGreen.progress,   // vert
                        ui.sbBlue.progress     // bleu
                    )
                    // modifier la couleur de l'échantillon de couleur
                    ui.ivColorSample.setBackgroundColor(color)
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {}
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
            }

            // attribuer l'écouteur aux curseurs
            ui.sbRed.setOnSeekBarChangeListener(listener)
            ui.sbGreen.setOnSeekBarChangeListener(listener)
            ui.sbBlue.setOnSeekBarChangeListener(listener)
            ui.sbAlpha.setOnSeekBarChangeListener(listener)
        }
    }
}
