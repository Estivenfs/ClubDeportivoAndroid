package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ActualizacionDatosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizacion_datos)

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Clientes"

        btnAtras.setOnClickListener {
            finish()
        }

        // Lógica de muestreo de calendario
        val inputFechaNacimiento = findViewById<TextView>(R.id.inputFechaNacimiento)
        val fechaNacimientoLayout = findViewById<LinearLayout>(R.id.fechaNacimientoLayout)


        fechaNacimientoLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaNacimiento) // Pasar el contexto de la actividad
        }



        // Lógica del boton actualizar cambios
        val btnActualizarCambios = findViewById<Button>(R.id.buttonActualizarDatos)

        btnActualizarCambios.setOnClickListener {
            val cambioExitosoDialog =
                CambioExitosoFragment.newInstance() // Usar el nuevo nombre de la clase
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
