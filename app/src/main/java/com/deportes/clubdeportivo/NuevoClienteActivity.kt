package com.deportes.clubdeportivo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NuevoClienteActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_cliente)

        // Mostrar el calendario
        val inputFechaNacimiento = findViewById<TextView>(R.id.inputFechaNacimiento)
        val fechaNacimientoLayout = findViewById<LinearLayout>(R.id.fechaNacimientoLayout)
        fechaNacimientoLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaNacimiento)
        }
        // Importación y lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Registrar Cliente"

        btnAtras.setOnClickListener {
            finish()
        }



        val btnRegistrarCliente: Button = findViewById<Button>(R.id.btnRegistrarCliente)



        btnRegistrarCliente.setOnClickListener {
            val registroExitosoDialog =
                RegistroExitosoFragment.newInstance() // Usar el nuevo nombre de la clase
            registroExitosoDialog.setOnVolverClickListener {
                // ... lógica al volver ...
            }
            registroExitosoDialog.show(
                supportFragmentManager,
                CambioExitosoFragment.TAG
            ) // Usar el nuevo TAG
        }
    }



}
