// PagosDetallesSocioActivity.kt
/*package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.deportes.clubdeportivo.db.BDatos
import java.text.SimpleDateFormat
import java.util.*

class PagosDetallesSocioActivity : AppCompatActivity() {
    private lateinit var db: BDatos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_detalles_socio)

        db = BDatos(this)

        val idCliente = intent.getIntExtra("id_cliente", -1)
        val nombreCliente = intent.getStringExtra("nombre") ?: ""
        val apellidoCliente = intent.getStringExtra("apellido") ?: ""
        val dniCliente = intent.getStringExtra("dni") ?: ""

        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Detalles de Pago (Socio)"
        btnAtras.setOnClickListener { finish() }

        val inputNombre: EditText = findViewById(R.id.inputNombre)
        val inputApellido: EditText = findViewById(R.id.inputApellido)
        val inputDNI: EditText = findViewById(R.id.inputDNI)
        val inputFechaPago: TextView = findViewById(R.id.inputFechaPago)
        val inputFechaInicio : TextView = findViewById(R.id.inputFechaInicio)
        val textMeses = findViewById<TextView>(R.id.textMesesSuscripcionSeleccionada)
        val textCuota = findViewById<TextView>(R.id.textTipoCuotaSeleccionada)
        val textPago = findViewById<TextView>(R.id.textOpcionesPagoSeleccionada)
        val btnCalcular: Button = findViewById(R.id.btnCalcularPagoSocio)
        val layoutFechaDePago = findViewById<LinearLayout>(R.id.fechaPagoLayout)
        val layoutFechaDeInicio = findViewById<LinearLayout>(R.id.fechaInicioLayout)
        val fechaHoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        inputFechaPago.text = fechaHoy
        inputFechaInicio.text = fechaHoy

        layoutFechaDePago.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaPago)
        }

        layoutFechaDeInicio.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaInicio)
        }


        inputNombre.setText(nombreCliente)
        inputApellido.setText(apellidoCliente)
        inputDNI.setText(dniCliente)

        inputNombre.isEnabled = false
        inputApellido.isEnabled = false
        inputDNI.isEnabled = false



        btnCalcular.setOnClickListener {
            val meses = textMeses.text.toString().toIntOrNull() ?: 1
            val tipo = textCuota.text.toString()
            val metodo = textPago.text.toString()
            var monto = when (tipo) {
                "Premium" -> 1500 * meses
                else -> 1000 * meses
            }.toDouble()
            if (metodo == "3 Cuotas") monto *= 1.05
            if (metodo == "6 Cuotas") monto *= 1.10

            val insertarPago = """
                INSERT INTO Pagos (id_cliente, monto, cantidad_cuotas, medio_pago, fecha_pago)
                VALUES (?, ?, ?, ?, ?)
            """.trimIndent()
            val args = arrayOf(idCliente.toString(), monto.toString(), meses.toString(), metodo, fechaHoy)
            val idPago = db.insertar(insertarPago, args)

            if (idPago != -1) {
                val intent = Intent(this, ComprobanteDePago::class.java)
                intent.putExtra("id_pago", idPago)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error al registrar el pago", Toast.LENGTH_SHORT).show()
            }
        }
    }
}*/
package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.view.View
import android.widget.EditText // Importar EditText
import com.deportes.clubdeportivo.db.BDatos // Importar tu clase BDatos
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.widget.Toast // Para mostrar mensajes al usuario
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deportes.clubdeportivo.adapters.OptionAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PagosDetallesSocioActivity : AppCompatActivity() {

    // Instancia de la base de datos
    private lateinit var db: BDatos

    // Referencias a los componentes de la vista para poder acceder a ellos fácilmente
    private lateinit var inputNombre: EditText
    private lateinit var inputApellido: EditText
    private lateinit var inputDNI: EditText
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

        // Obtener referencias a las vistas
        val inputNombre = findViewById<EditText>(R.id.inputNombre)
        val inputApellido = findViewById<EditText>(R.id.inputApellido)
        val inputDNI = findViewById<EditText>(R.id.inputDNI)

        // Obtener datos del intent
        idCliente = intent.getIntExtra("id_cliente", -1)
        nombre = intent.getStringExtra("nombre") ?: ""
        apellido = intent.getStringExtra("apellido") ?: ""
        dni = intent.getStringExtra("dni") ?: ""

        // Mostrar datos en los campos
        inputNombre.setText(nombre)
        inputApellido.setText(apellido)
        inputDNI.setText(dni)
        inputNombre.isEnabled = false
        inputApellido.isEnabled = false
        inputDNI.isEnabled = false

        // Inicializar la base de datos
        db = BDatos(this)

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Detalles de Pago (Socio)"

        btnAtras.setOnClickListener {
            finish()
        }

        // Componentes de la vista - OBTENER REFERENCIAS
        //inputNombre = findViewById(R.id.inputNombre)
        //inputApellido = findViewById(R.id.inputApellido)
        //inputDNI = findViewById(R.id.inputDNI)
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

        //Configuracion de los inputFecha para que por defecto tenga la fecha de hoy
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaActual = dateFormat.format(Date())
        inputFechaPago.text = fechaActual
        inputFechaInicio.text = fechaActual

        // Configuración de los spinners (BottomSheet selectors)
        val opcionesPeriodoAPagar = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
        val opcionesTipoDeCuota = listOf("Regular", "Premium")
        val opcionesDePago = listOf("Efectivo (-10%)", "3 Cuotas (-5%)", "6 Cuotas")

        // Configurar los listeners para los spinners
        setOnClickListener(layoutMeses, opcionesPeriodoAPagar, "Meses de suscripción", textMesesSuscripcionSeleccionada)
        setOnClickListener(layoutTipoCuota, opcionesTipoDeCuota, "Tipo de cuota", textTipoCuotaSeleccionada)
        setOnClickListener(layoutOpcionesPago, opcionesDePago, "Opciones de pago", textOpcionesPagoSeleccionada)

        // Configurar los listeners para los layouts de fecha
        fechaPagoLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaPago)
        }
        fechaInicioLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaInicio)
        }

        // Configuración del botón Calcular
        btnCalcular.setOnClickListener {

            calcularDetallesPago()

        }
    }

    // --- Función para almacenar los datos en la base de datos usando métodos genéricos ---
    private fun calcularDetallesPago() {
        val nombre = nombre
        val apellido = apellido
        val dni = dni
        val fechaPagoStr = inputFechaPago.text.toString().trim()
        val fechaInicioStr = inputFechaInicio.text.toString().trim() // Para la tabla Cliente
        val mesesSuscripcion = textMesesSuscripcionSeleccionada.text.toString().trim().toIntOrNull() ?: 1
        val tipoCuota = textTipoCuotaSeleccionada.text.toString().trim()
        val opcionPago = textOpcionesPagoSeleccionada.text.toString().trim()

        // Validaciones básicas
        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || fechaPagoStr.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos obligatorios.", Toast.LENGTH_SHORT).show()
            return
        }
        if (mesesSuscripcion <= 0) {
            Toast.makeText(this, "Los meses de suscripción deben ser al menos 1.", Toast.LENGTH_SHORT).show()
            return
        }

        // Formatear fechas para la base de datos (YYYY-MM-DD es común para DATE en SQLite)
        // O mantén DD/MM/YYYY si ese es tu formato preferido y lo manejas en la DB.
        val dateFormatDb = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateFormatDisplay = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val fechaActual = dateFormatDisplay.format(Date())

        val fechaPagoFormateada: String
        try {
            val datePago = if (fechaPagoStr == "10/04/2025" || fechaPagoStr.isEmpty()) Date() else dateFormatDisplay.parse(fechaPagoStr)
            fechaPagoFormateada = dateFormatDb.format(datePago!!)
        } catch (e: Exception) {
            Toast.makeText(this, "Formato de fecha de pago inválido.", Toast.LENGTH_SHORT).show()
            return
        }

        val fechaInicioFormateada: String
        try {
            val dateInicio = if (fechaInicioStr == "10/04/2025" || fechaInicioStr.isEmpty()) Date() else dateFormatDisplay.parse(fechaInicioStr)
            fechaInicioFormateada = dateFormatDb.format(dateInicio!!)
        } catch (e: Exception) {
            Toast.makeText(this, "Formato de fecha de inicio inválido.", Toast.LENGTH_SHORT).show()
            return
        }
        //A fecha inicio sumo la cantidad de meses de suscripcion
        val fechaInicioDate = dateFormatDb.parse(fechaInicioFormateada)
        val calendar = java.util.Calendar.getInstance()
        calendar.time = fechaInicioDate
        calendar.add(java.util.Calendar.MONTH, mesesSuscripcion)
        val fechaExpiracion = calendar.time
        val fechaExpiracionFormateada = dateFormatDb.format(fechaExpiracion)

        // Lógica de cálculo de monto (ejemplo, ajusta esto a tu negocio)
        var monto = 0.0
        when (tipoCuota) {
            "Regular" -> monto = 30000.0 * mesesSuscripcion
            "Premium" -> monto = 40000.0 * mesesSuscripcion
        }
        val montoBase = monto

        // Aplicar descuentos o recargos según la opción de pago
        when (opcionPago) {
            "3 Cuotas (-5%)" -> monto *= 0.95
            "6 Cuotas" -> monto
            "Efectivo (-10%)" -> monto *= 0.90
        }

        // Crear Intent y pasar datos
        val intent = Intent(this, DetallePagoActivity::class.java).apply {
            putExtra("idCliente", idCliente)
            putExtra("nombre", nombre)
            putExtra("apellido", apellido)
            putExtra("dni", dni)
            putExtra("fechaPago", fechaPagoFormateada)
            putExtra("fechaInicio", fechaInicioFormateada)
            putExtra("mesesSuscripcion", mesesSuscripcion)
            putExtra("tipoCuota", tipoCuota)
            putExtra("opcionPago", opcionPago)
            putExtra("monto", monto.toString())
            putExtra("fechaExpiracion", fechaExpiracionFormateada)
            putExtra("montoBase", montoBase.toString())
            putExtra("actividad", "Membresía")
            putExtra("tipoCliente", "Socio")
        }

        startActivity(intent)


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