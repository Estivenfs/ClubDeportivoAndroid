package com.deportes.clubdeportivo

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ModificarActividadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar_actividad)

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Modificar Actividad"

        btnAtras.setOnClickListener {
            finish()
        }
        // Lógica del botón modificar
        val btnModificarActividad = findViewById<Button>(R.id.btnModificar)

        btnModificarActividad.setOnClickListener {
            val cambioExitosoDialog =
                CambioExitosoFragment.newInstance()
            cambioExitosoDialog.setOnVolverClickListener {
                // ... lógica al volver ...
            }
            cambioExitosoDialog.show(
                supportFragmentManager,
                CambioExitosoFragment.TAG
            ) // Usar el nuevo TAG
        }

    }
}