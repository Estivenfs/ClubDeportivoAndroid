package com.deportes.clubdeportivo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deportes.clubdeportivo.adapters.OptionAdapter
import com.deportes.clubdeportivo.db.BDatos
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NuevoClienteActivity : AppCompatActivity() {
    private lateinit var db: BDatos
    private lateinit var inputFechaNacimiento : TextView
    private lateinit var textTipoInscripcionSeleccionada: TextView
    private lateinit var textAptoFisicoSeleccionado: TextView
    private lateinit var inputNombre: TextView
    private lateinit var inputApellido: TextView
    private lateinit var inputDNI: TextView
    private lateinit var inputEmail: TextView
    private lateinit var inputTelefono: TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_cliente)

        // Mostrar el calendario
        inputFechaNacimiento = findViewById<TextView>(R.id.inputFechaNacimiento)
        val fechaNacimientoLayout = findViewById<LinearLayout>(R.id.fechaNacimientoLayout)
        fechaNacimientoLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaNacimiento)
        }
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Registrar Cliente"

        // Importación y lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        btnAtras.setOnClickListener {
            finish()
        }

        // Inicializar la base de datos
        db = BDatos(this)

        // Configuración de los campos de texto
        inputNombre = findViewById<TextView>(R.id.inputNombre)
        inputApellido = findViewById<TextView>(R.id.inputApellido)
        inputDNI = findViewById<TextView>(R.id.inputDNI)
        inputEmail = findViewById<TextView>(R.id.inputEmail)
        inputTelefono = findViewById<TextView>(R.id.inputTelefono)

        // Configuración de los spinners (BottomSheet selectors)
        val spinnerTipoDeInscripcion = findViewById<LinearLayout>(R.id.spinnerTipoDeInscripcion)
        val spinnerAptoFisico = findViewById<LinearLayout>(R.id.spinnerAptoFisico)
        textTipoInscripcionSeleccionada = findViewById<TextView>(R.id.inputTipoInscripcion)
        textAptoFisicoSeleccionado = findViewById<TextView>(R.id.inputAptoFisico)

        // Opciones para los spinners
        val opcionesTipoDeInscripcion = listOf("Socio", "No Socio")
        val opcionesAptoFisico = listOf("Posee", "No posee")

        // Configurar los listeners para los spinners
        setOnClickListener(spinnerTipoDeInscripcion, opcionesTipoDeInscripcion, "Tipo de Inscripción", textTipoInscripcionSeleccionada)
        setOnClickListener(spinnerAptoFisico, opcionesAptoFisico, "Apto Físico", textAptoFisicoSeleccionado)




        val btnRegistrarCliente: Button = findViewById<Button>(R.id.btnRegistrarCliente)



        btnRegistrarCliente.setOnClickListener {
            val registroExitosoDialog =
                RegistroExitosoFragment.newInstance() // Usar el nuevo nombre de la clase
            registroExitosoDialog.setOnVolverClickListener {
                // ... lógica al volver ...
            }
            registroExitosoDialog.show(
                supportFragmentManager,
                CambioExitosoFragment.TAG
            ) // Usar el nuevo TAG
        }
    }

    private fun guardarNuevoCliente() {
       /* val nombre = inputNombre.text.toString().trim()
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
        } */
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
