package com.deportes.clubdeportivo

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PagosDetallesNoSocioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_detalles_no_socio)


        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Detalles de Pago (No Socio)"

        btnAtras.setOnClickListener {
            finish()
        }

        val inputFechaPago = findViewById<TextView>(R.id.inputFechaPago)
        val inputFechaAPagar = findViewById<TextView>(R.id.inputFechaAPagar)
        val fechaPagoLayout = findViewById<LinearLayout>(R.id.fechaPagoLayout)
        val fechaAPagarLayout = findViewById<LinearLayout>(R.id.fechaAPagarLayout)

        fechaPagoLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaPago) // Pasar el contexto de la actividad
        }
        fechaAPagarLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaAPagar) // Pasar el contexto de la actividad
        }
    }


}