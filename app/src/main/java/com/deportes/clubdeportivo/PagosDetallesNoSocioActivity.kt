package com.deportes.clubdeportivo

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
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

class PagosDetallesNoSocioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pagos_detalles_no_socio)

        val inputFechaPago = findViewById<TextView>(R.id.inputFechaPago)
        val inputFechaAPagar = findViewById<TextView>(R.id.inputFechaAPagar)
        val fechaPagoLayout = findViewById<LinearLayout>(R.id.fechaPagoLayout)
        val fechaAPagarLayout = findViewById<LinearLayout>(R.id.fechaAPagarLayout)

        fechaPagoLayout.setOnClickListener {
            mostrarDatePickerDialog(this, inputFechaPago) // Pasar el contexto de la actividad
        }
        fechaAPagarLayout.setOnClickListener {
            mostrarDatePickerDialog(this, inputFechaAPagar) // Pasar el contexto de la actividad
        }

        // Importación y lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Detalle del pago"

        btnAtras.setOnClickListener {
            finish()
        }

        // Importacion y lógica de barra inferior
        val btnMenu = findViewById<LinearLayout>(R.id.btnMenu)
        val btnConsultas = findViewById<LinearLayout>(R.id.btnConsultas)
        val btnPagos = findViewById<LinearLayout>(R.id.btnPagos)
        val btnClientes = findViewById<LinearLayout>(R.id.btnNuevoCliente)

        // Asignar este valor a la pantalla en la que se encuentre
        // btnMenu.alpha = 0.5f // Establece la transparencia al 50%
        // btnMenu.isEnabled = false // Opcionalmente, desactiva los clics

        btnMenu.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
        }

        btnConsultas.setOnClickListener {
            startActivity(Intent(this, ConsultasActivity::class.java))
        }

        btnPagos.setOnClickListener {
            startActivity(Intent(this, PagosBusquedaActivity::class.java))
        }

        btnClientes.setOnClickListener {
            startActivity(Intent(this, NuevoClienteActivity::class.java))
        }
    }

    private fun mostrarDatePickerDialog(context: Context, textView: TextView) { // Cambio a TextView
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(context, // Usar el contexto pasado como parámetro
            { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                    Calendar.getInstance().apply {
                        set(year, monthOfYear, dayOfMonth)
                    }.time
                )
                textView.text = selectedDate // Actualizar el TextView
            }, year, month, day
        )
        dpd.show()
    }
}