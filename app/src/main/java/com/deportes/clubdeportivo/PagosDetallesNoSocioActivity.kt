/*package com.deportes.clubdeportivo

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


}*/
package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deportes.clubdeportivo.adapters.OptionAdapter
import com.deportes.clubdeportivo.db.BDatos
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PagosDetallesNoSocioActivity : AppCompatActivity() {
    // Instancia de la base de datos
    private lateinit var db: BDatos

    // Referencias a los componentes de la vista para poder acceder a ellos fácilmente
    private lateinit var inputNombre: EditText
    private lateinit var inputApellido: EditText
    private lateinit var inputDNI: EditText
    private lateinit var inputFechaPago: TextView
    private lateinit var inputFechaAPagar: TextView
    private lateinit var textActividadesSeleccionada: TextView
    private lateinit var textOpcionesPagoSeleccionada: TextView
    private lateinit var nombre: String
    private lateinit var apellido: String
    private lateinit var dni: String
    private var idCliente: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_detalles_no_socio)

        // Obtener referencias a las vistas
        inputNombre = findViewById<EditText>(R.id.inputNombre)
        inputApellido = findViewById<EditText>(R.id.inputApellido)
        inputDNI = findViewById<EditText>(R.id.inputDNI)
        val btnCalcular = findViewById<TextView>(R.id.btnCalcularPagoNoSocio)

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

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Detalles de Pago (No Socio)"

        btnAtras.setOnClickListener {
            finish()
        }
        // Componentes de la vista
        inputFechaPago = findViewById<TextView>(R.id.inputFechaPago)
        inputFechaAPagar = findViewById<TextView>(R.id.inputFechaAPagar)
        val fechaPagoLayout = findViewById<LinearLayout>(R.id.fechaPagoLayout)
        val fechaAPagarLayout = findViewById<LinearLayout>(R.id.fechaAPagarLayout)
        val spinnerActividades = findViewById<LinearLayout>(R.id.spinnerActividades)
        val spinnerOpcionesPago = findViewById<LinearLayout>(R.id.spinnerOpcionesPago)
        textActividadesSeleccionada = findViewById<TextView>(R.id.textActividadesSeleccionada)
        textOpcionesPagoSeleccionada = findViewById<TextView>(R.id.textOpcionesPagoSeleccionada)

        val fechaHoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        inputFechaPago.text = fechaHoy
        inputFechaAPagar.text = fechaHoy


        // Instancia de la base de datos
        val db = BDatos(this)

        // Consultas a la base de datos
        val query = "SELECT nombre_actividad FROM Actividades WHERE inscriptos < cupo_actividad"
        val resultado = db.ejecutarConsultaSelect(query)
        var optionsActividades = listOf("No hay actividades disponibles")

        // Procesamiento de los resultados
        if (resultado.isNotEmpty()) {

            optionsActividades = resultado.map { it["nombre_actividad"] as String }

        }

        // Configuración de los spinners
        val opcionesDePago = listOf("Efectivo (-10%)", "3 Cuotas (-5%)", "6 Cuotas")

        // Configurar los listeners para los spinners
        setOnClickListener(spinnerActividades, optionsActividades, "Actividades", textActividadesSeleccionada)
        setOnClickListener(spinnerOpcionesPago, opcionesDePago, "Opciones de pago", textOpcionesPagoSeleccionada)


        fechaPagoLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaPago) // Pasar el contexto de la actividad
        }
        fechaAPagarLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaAPagar) // Pasar el contexto de la actividad
        }


        btnCalcular.setOnClickListener {
            calcularDetallesPago()

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

    private fun calcularDetallesPago() {
        val nombre = nombre
        val apellido = apellido
        val dni = dni
        val fechaPagoStr = inputFechaPago.text.toString().trim()
        val fechaInicioStr = inputFechaAPagar.text.toString().trim() // Para la tabla Cliente
        val actividad = textActividadesSeleccionada.text.toString().trim()
        val opcionPago = textOpcionesPagoSeleccionada.text.toString().trim()
        if(actividad == "Seleccione actividad"){
            Toast.makeText(this, "Por favor, seleccione una actividad.", Toast.LENGTH_SHORT).show()
            return
        }
        // Obtengo el precio de la actividad de la base de datos
        val db = BDatos(this)
        val query = "SELECT precio_actividad FROM Actividades WHERE nombre_actividad = ?"
        val resultado = db.ejecutarConsultaSelect(query, arrayOf(actividad))
        var precioActividad = 0.0f
        if (resultado.isNotEmpty()) {
            precioActividad = resultado[0]["precio_actividad"] as Float
        }

        // Validaciones básicas
        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || fechaPagoStr.isEmpty() || actividad.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos obligatorios.", Toast.LENGTH_SHORT).show()
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


        // Lógica de cálculo de monto (ejemplo, ajusta esto a tu negocio)
        var monto = precioActividad
        val montoBase = monto

        // Aplicar descuentos o recargos según la opción de pago
        when (opcionPago) {
            "3 Cuotas (-5%)" -> monto *= 0.95f
            "6 Cuotas" -> monto
            "Efectivo (-10%)" -> monto *= 0.90f
        }

        // Crear Intent y pasar datos
        val intent = Intent(this, DetallePagoActivity::class.java).apply {
            putExtra("idCliente", idCliente)
            putExtra("nombre", nombre)
            putExtra("apellido", apellido)
            putExtra("dni", dni)
            putExtra("fechaPago", fechaPagoFormateada)
            putExtra("fechaInicio", fechaInicioFormateada)
            putExtra("mesesSuscripcion", 0)
            putExtra("opcionPago", opcionPago)
            putExtra("monto", monto.toString())
            putExtra("fechaExpiracion", "")
            putExtra("montoBase", montoBase.toString())
            putExtra("actividad", actividad)
            putExtra("tipoCliente", "No Socio")
        }

        startActivity(intent)


    }


}
