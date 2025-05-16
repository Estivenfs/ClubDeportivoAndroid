package com.deportes.clubdeportivo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ModificarActividadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar_actividad)


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