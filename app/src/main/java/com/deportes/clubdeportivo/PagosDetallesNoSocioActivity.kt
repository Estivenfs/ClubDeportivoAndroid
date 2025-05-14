package com.deportes.clubdeportivo

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
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
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaPago) // Pasar el contexto de la actividad
        }
        fechaAPagarLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaAPagar) // Pasar el contexto de la actividad
        }
    }


}