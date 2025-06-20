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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_detalles_socio)

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
        inputNombre = findViewById(R.id.inputNombre)
        inputApellido = findViewById(R.id.inputApellido)
        inputDNI = findViewById(R.id.inputDNI)
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
        val opcionesDePago = listOf("Efectivo", "3 Cuotas", "6 Cuotas")

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
            guardarDetallesDePago()
            // Después de guardar, puedes redirigir o hacer algo más
            startActivity(Intent(this, DetallePagoActivity::class.java))
        }
    }

    // --- Función para almacenar los datos en la base de datos usando métodos genéricos ---
    private fun guardarDetallesDePago() {
        val nombre = inputNombre.text.toString().trim()
        val apellido = inputApellido.text.toString().trim()
        val dni = inputDNI.text.toString().trim()
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


        // Lógica de cálculo de monto (ejemplo, ajusta esto a tu negocio)
        var monto = 0.0
        when (tipoCuota) {
            "Regular" -> monto = 1000.0 * mesesSuscripcion
            "Premium" -> monto = 1500.0 * mesesSuscripcion
        }

        // Aplicar descuentos o recargos según la opción de pago
        when (opcionPago) {
            "3 Cuotas" -> monto *= 1.05 // Recargo del 5%
            "6 Cuotas" -> monto *= 1.10 // Recargo del 10%
            // "Efectivo" no tiene recargo
        }

        try {
            var idCliente: Int? = null

            // 1. Buscar si el cliente ya existe por DNI
            val queryCliente = "SELECT id_cliente FROM Cliente WHERE dni = ?"
            val argsCliente = arrayOf(dni)
            val resultadoCliente = db.ejecutarConsultaSelect(queryCliente, argsCliente)

            if (resultadoCliente.isNotEmpty()) {
                // Cliente existe, obtener su ID
                idCliente = resultadoCliente[0]["id_cliente"] as? Int
                Toast.makeText(this, "Cliente existente encontrado. ID: $idCliente", Toast.LENGTH_SHORT).show()
            } else {
                // Mostrar  que el cliente no existe
                Toast.makeText(this, "Cliente no encontrado.", Toast.LENGTH_SHORT).show()
            }

            // 2. Insertar el pago
            if (idCliente != null && idCliente != -1) {
                // Columnas: id_cliente, monto, cantidad_cuotas, medio_pago, fecha_pago
                val insertPagoQuery = """
                    INSERT INTO Pagos (id_cliente, monto, cantidad_cuotas, medio_pago, fecha_pago)
                    VALUES (?, ?, ?, ?, ?)
                """.trimIndent()
                val insertPagoArgs = arrayOf(
                    idCliente.toString(),
                    monto.toString(),
                    mesesSuscripcion.toString(),
                    opcionPago,
                    fechaPagoFormateada
                )
                val idPago = db.insertar(insertPagoQuery, insertPagoArgs)

                if (idPago != -1) {
                    Toast.makeText(this, "Pago registrado exitosamente. ID: $idPago", Toast.LENGTH_LONG).show()
                    // Si tienes una actividad a la que pasarle los detalles de pago, puedes hacerlo aquí
                    // val intent = Intent(this, DetallePagoActivity::class.java)
                    // intent.putExtra("pago_id", idPago)
                    // startActivity(intent)
                } else {
                    Toast.makeText(this, "Error al registrar el pago.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "No se pudo obtener/crear el ID del cliente.", Toast.LENGTH_LONG).show()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Ocurrió un error: ${e.message}", Toast.LENGTH_LONG).show()
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
