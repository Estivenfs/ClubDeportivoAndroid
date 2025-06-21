package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deportes.clubdeportivo.adapters.OptionAdapter
import com.deportes.clubdeportivo.db.BDatos
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*

class PagosDetallesNoSocioActivity : AppCompatActivity() {

    private lateinit var db: BDatos
    private lateinit var nombre: String
    private lateinit var apellido: String
    private lateinit var dni: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_detalles_no_socio)

        db = BDatos(this)

        nombre = intent.getStringExtra("nombre") ?: ""
        apellido = intent.getStringExtra("apellido") ?: ""
        dni = intent.getStringExtra("dni") ?: ""

        val inputNombre = findViewById<EditText>(R.id.inputNombre)
        val inputApellido = findViewById<EditText>(R.id.inputApellido)
        val inputDNI = findViewById<EditText>(R.id.inputDNI)
        val inputFechaPago = findViewById<TextView>(R.id.inputFechaPago)
        val inputFechaAPagar = findViewById<TextView>(R.id.inputFechaAPagar)
        val fechaPagoLayout = findViewById<LinearLayout>(R.id.fechaPagoLayout)
        val fechaAPagarLayout = findViewById<LinearLayout>(R.id.fechaAPagarLayout)
        val spinnerActividades = findViewById<LinearLayout>(R.id.spinnerActividades)
        val spinnerOpcionesPago = findViewById<LinearLayout>(R.id.spinnerOpcionesPago)
        val textActividadesSeleccionada = findViewById<TextView>(R.id.textActividadesSeleccionada)
        val textOpcionesPagoSeleccionada = findViewById<TextView>(R.id.textOpcionesPagoSeleccionada)
        val btnCalcular = findViewById<Button>(R.id.btnCalcularPagoNoSocio)

        inputNombre.setText(nombre)
        inputApellido.setText(apellido)
        inputDNI.setText(dni)
        inputNombre.isEnabled = false
        inputApellido.isEnabled = false
        inputDNI.isEnabled = false

        val fechaHoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        inputFechaPago.text = fechaHoy
        inputFechaAPagar.text = fechaHoy

        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Detalles de Pago (No Socio)"
        btnAtras.setOnClickListener { finish() }

        val resultado = db.ejecutarConsultaSelect("SELECT nombre_actividad FROM Actividades")
        val optionsActividades = if (resultado.isNotEmpty())
            resultado.map { it["nombre_actividad"] as String }
        else listOf("No hay actividades disponibles")

        val opcionesDePago = listOf("Efectivo", "3 Cuotas", "6 Cuotas")

        setOnClickListener(spinnerActividades, optionsActividades, "Actividades", textActividadesSeleccionada)
        setOnClickListener(spinnerOpcionesPago, opcionesDePago, "Opciones de pago", textOpcionesPagoSeleccionada)

        fechaPagoLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaPago)
        }
        fechaAPagarLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaAPagar)
        }

        btnCalcular.setOnClickListener {
            val actividadSeleccionada = textActividadesSeleccionada.text.toString()
            val formaPagoSeleccionada = textOpcionesPagoSeleccionada.text.toString()
            val fechaPago = inputFechaPago.text.toString()
            val fechaAPagar = inputFechaAPagar.text.toString()

            if (actividadSeleccionada == "Seleccione actividad" || actividadSeleccionada == "No hay actividades disponibles") {
                Toast.makeText(this, "Seleccione una actividad válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val precio = db.obtenerPrecioActividad(actividadSeleccionada)

            val montoFinal = when (formaPagoSeleccionada) {
                "3 Cuotas" -> precio * 1.10f
                "6 Cuotas" -> precio * 1.20f
                else -> precio
            }

            val intent = Intent(this, DetallePagoActivity::class.java).apply {
                putExtra("nombre", nombre)
                putExtra("apellido", apellido)
                putExtra("dni", dni)
                putExtra("actividad", actividadSeleccionada)
                putExtra("formaPago", formaPagoSeleccionada)
                putExtra("fechaPago", fechaPago)
                putExtra("fechaAPagar", fechaAPagar)
                putExtra("monto", montoFinal)
                putExtra("tipoCliente", "No Socio")
            }
            startActivity(intent)
        }
    }

    private fun setOnClickListener(layout: LinearLayout, options: List<String>, title: String, textView: TextView) {
        layout.setOnClickListener {
            showBottomSheetSelector(title, options) { seleccion ->
                textView.text = seleccion
            }
        }
    }

    private fun showBottomSheetSelector(
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
