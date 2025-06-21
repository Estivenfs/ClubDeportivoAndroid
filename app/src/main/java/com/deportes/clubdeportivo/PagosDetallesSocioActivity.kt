package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.deportes.clubdeportivo.db.BDatos
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deportes.clubdeportivo.adapters.OptionAdapter
import java.text.SimpleDateFormat
import java.util.*

class PagosDetallesSocioActivity : AppCompatActivity() {

    private lateinit var db: BDatos

    private lateinit var inputFechaPago: TextView
    private lateinit var inputFechaInicio: TextView
    private lateinit var textMesesSuscripcionSeleccionada: TextView
    private lateinit var textTipoCuotaSeleccionada: TextView
    private lateinit var textOpcionesPagoSeleccionada: TextView

    private lateinit var nombre: String
    private lateinit var apellido: String
    private lateinit var dni: String
    private var idCliente: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_detalles_socio)

        val inputNombre = findViewById<EditText>(R.id.inputNombre)
        val inputApellido = findViewById<EditText>(R.id.inputApellido)
        val inputDNI = findViewById<EditText>(R.id.inputDNI)

        idCliente = intent.getIntExtra("id_cliente", -1)
        nombre = intent.getStringExtra("nombre") ?: ""
        apellido = intent.getStringExtra("apellido") ?: ""
        dni = intent.getStringExtra("dni") ?: ""

        inputNombre.setText(nombre)
        inputApellido.setText(apellido)
        inputDNI.setText(dni)
        inputNombre.isEnabled = false
        inputApellido.isEnabled = false
        inputDNI.isEnabled = false

        db = BDatos(this)

        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Detalles de Pago (Socio)"
        btnAtras.setOnClickListener { finish() }

        inputFechaPago = findViewById(R.id.inputFechaPago)
        inputFechaInicio = findViewById(R.id.inputFechaInicio)
        val fechaPagoLayout = findViewById<LinearLayout>(R.id.fechaPagoLayout)
        val fechaInicioLayout = findViewById<LinearLayout>(R.id.fechaInicioLayout)
        val btnCalcular = findViewById<Button>(R.id.btnCalcularPagoSocio)
        val layoutMeses = findViewById<LinearLayout>(R.id.spinnerMesesSuscripcion)
        textMesesSuscripcionSeleccionada = findViewById(R.id.textMesesSuscripcionSeleccionada)
        val layoutTipoCuota = findViewById<LinearLayout>(R.id.spinnerTipoDeCuota)
        textTipoCuotaSeleccionada = findViewById(R.id.textTipoCuotaSeleccionada)
        val layoutOpcionesPago = findViewById<LinearLayout>(R.id.spinnerOpcionesPago)
        textOpcionesPagoSeleccionada = findViewById(R.id.textOpcionesPagoSeleccionada)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaActual = dateFormat.format(Date())
        inputFechaPago.text = fechaActual
        inputFechaInicio.text = fechaActual

        val opcionesPeriodoAPagar = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
        val opcionesTipoDeCuota = listOf("Regular", "Premium")
        val opcionesDePago = listOf("Efectivo (-10%)", "3 Cuotas (-5%)", "6 Cuotas")

        setOnClickListener(layoutMeses, opcionesPeriodoAPagar, "Meses de suscripción", textMesesSuscripcionSeleccionada)
        setOnClickListener(layoutTipoCuota, opcionesTipoDeCuota, "Tipo de cuota", textTipoCuotaSeleccionada)
        setOnClickListener(layoutOpcionesPago, opcionesDePago, "Opciones de pago", textOpcionesPagoSeleccionada)

        fechaPagoLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaPago)
        }
        fechaInicioLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaInicio)
        }

        btnCalcular.setOnClickListener {
            calcularDetallesPago()
        }
    }

    private fun calcularDetallesPago() {
        val fechaPagoStr = inputFechaPago.text.toString().trim()
        val fechaInicioStr = inputFechaInicio.text.toString().trim()
        val mesesSuscripcion = textMesesSuscripcionSeleccionada.text.toString().trim().toIntOrNull() ?: 1
        val tipoCuota = textTipoCuotaSeleccionada.text.toString().trim()
        val opcionPago = textOpcionesPagoSeleccionada.text.toString().trim()

        if (mesesSuscripcion <= 0) {
            Toast.makeText(this, "Los meses de suscripción deben ser al menos 1.", Toast.LENGTH_SHORT).show()
            return
        }

        val dateFormatDb = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateFormatDisplay = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val fechaPagoFormateada = try {
            dateFormatDb.format(dateFormatDisplay.parse(fechaPagoStr) ?: Date())
        } catch (e: Exception) {
            Toast.makeText(this, "Formato de fecha de pago inválido.", Toast.LENGTH_SHORT).show()
            return
        }

        val fechaInicioFormateada = try {
            dateFormatDb.format(dateFormatDisplay.parse(fechaInicioStr) ?: Date())
        } catch (e: Exception) {
            Toast.makeText(this, "Formato de fecha de inicio inválido.", Toast.LENGTH_SHORT).show()
            return
        }

        val fechaInicioDate = dateFormatDb.parse(fechaInicioFormateada) ?: Date()
        val calendar = Calendar.getInstance()
        calendar.time = fechaInicioDate
        calendar.add(Calendar.MONTH, mesesSuscripcion)
        val fechaExpiracionFormateada = dateFormatDb.format(calendar.time)

        var monto = when (tipoCuota) {
            "Regular" -> 30000.0 * mesesSuscripcion
            "Premium" -> 40000.0 * mesesSuscripcion
            else -> 30000.0 * mesesSuscripcion
        }

        when (opcionPago) {
            "3 Cuotas (-5%)" -> monto *= 0.95
            "6 Cuotas" -> {} // Sin descuento ni recargo
            "Efectivo (-10%)" -> monto *= 0.90
        }

        val intent = Intent(this, DetallePagoActivity::class.java).apply {
            putExtra("idCliente", idCliente)
            putExtra("nombre", nombre)
            putExtra("apellido", apellido)
            putExtra("dni", dni)
            putExtra("fechaPago", fechaPagoFormateada)
            putExtra("fechaAPagar", fechaExpiracionFormateada)
            putExtra("mesesSuscripcion", mesesSuscripcion)
            putExtra("tipoCuota", tipoCuota)
            putExtra("formaPago", opcionPago)
            putExtra("monto", monto.toFloat())
            putExtra("actividad", "Membresía")
            putExtra("tipoCliente", "Socio")
        }
        startActivity(intent)
    }

    private fun setOnClickListener(layout: LinearLayout, options: List<String>, title: String, textView: TextView) {
        layout.setOnClickListener {
            showBottomSheetSelector(title, options) { seleccion ->
                textView.text = seleccion
            }
        }
    }

    private fun showBottomSheetSelector(title: String, options: List<String>, onSelected: (String) -> Unit) {
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
