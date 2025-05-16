package com.deportes.clubdeportivo

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PagosDetallesSocioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_detalles_socio)

        val inputFechaPago = findViewById<TextView>(R.id.inputFechaPago)
        val inputFechaInicio = findViewById<TextView>(R.id.inputFechaInicio)
        val fechaPagoLayout = findViewById<LinearLayout>(R.id.fechaPagoLayout)
        val fechaInicioLayout = findViewById<LinearLayout>(R.id.fechaInicioLayout)

        fechaPagoLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaPago) // Pasar el contexto de la actividad
        }
        fechaInicioLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaInicio) // Pasar el contexto de la actividad
        }
    }

}