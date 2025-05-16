package com.deportes.clubdeportivo

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ActualizacionDatosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizacion_datos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
            val registroExitosoDialog =
                RegistroExitosoFragment.newInstance() // Usar el nuevo nombre de la clase
            registroExitosoDialog.setOnVolverClickListener {
                // ... lógica al volver ...
            }
            registroExitosoDialog.show(
                supportFragmentManager,
                RegistroExitosoFragment.TAG
            ) // Usar el nuevo TAG
        }
    }



}
