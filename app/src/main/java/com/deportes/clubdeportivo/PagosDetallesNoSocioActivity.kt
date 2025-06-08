package com.deportes.clubdeportivo

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deportes.clubdeportivo.adapters.OptionAdapter
import com.deportes.clubdeportivo.db.BDatos
import com.google.android.material.bottomsheet.BottomSheetDialog

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
        // Componentes de la vista
        val inputFechaPago = findViewById<TextView>(R.id.inputFechaPago)
        val inputFechaAPagar = findViewById<TextView>(R.id.inputFechaAPagar)
        val fechaPagoLayout = findViewById<LinearLayout>(R.id.fechaPagoLayout)
        val fechaAPagarLayout = findViewById<LinearLayout>(R.id.fechaAPagarLayout)
        val spinnerActividades = findViewById<LinearLayout>(R.id.spinnerActividades)
        val spinnerOpcionesPago = findViewById<LinearLayout>(R.id.spinnerOpcionesPago)
        val textActividadesSeleccionada = findViewById<TextView>(R.id.textActividadesSeleccionada)
        val textOpcionesPagoSeleccionada = findViewById<TextView>(R.id.textOpcionesPagoSeleccionada)
        // Instancia de la base de datos
        val db = BDatos(this)

        // Consultas a la base de datos
        val query = "SELECT nombre_actividad FROM Actividades"
        val resultado = db.ejecutarConsultaSelect(query)
        var optionsActividades = listOf("No hay actividades disponibles")

        // Procesamiento de los resultados
        if (resultado.isNotEmpty()) {
            optionsActividades = resultado.map { it["nombre_actividad"] as String }

        }

        // Configuración de los spinners
        val opcionesDePago = listOf("Efectivo", "3 Cuotas", "6 Cuotas")

        // Configurar los listeners para los spinners
        setOnClickListener(spinnerActividades, optionsActividades, "Actividades", textActividadesSeleccionada)
        setOnClickListener(spinnerOpcionesPago, opcionesDePago, "Opciones de pago", textOpcionesPagoSeleccionada)


        fechaPagoLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaPago) // Pasar el contexto de la actividad
        }
        fechaAPagarLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaAPagar) // Pasar el contexto de la actividad
        }
    }

    fun setOnClickListener(layout: LinearLayout, options: List<String>, title: String, textView: TextView) {
        layout.setOnClickListener {
            showBottomSheetSelector(title, options) { seleccion ->
                textView.text = seleccion
            }
        }

    }
    fun showBottomSheetSelector(
        title: String,
        options: List<String>,
        onSelected: (String) -> Unit
    ) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_selector, null)

        val titleText = view.findViewById<TextView>(R.id.titleBottomSheet)
        val recyclerView = view.findViewById<RecyclerView>(R.id.listOptions)

        titleText.text = title
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = OptionAdapter(options) {
            onSelected(it)
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }


}