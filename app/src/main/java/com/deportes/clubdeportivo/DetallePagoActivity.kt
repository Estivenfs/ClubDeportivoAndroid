package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.deportes.clubdeportivo.db.BDatos

class DetallePagoActivity : AppCompatActivity() {

    private lateinit var db: BDatos

    private lateinit var nombre: String
    private lateinit var apellido: String
    private lateinit var dni: String
    private lateinit var fechaPago: String
    private lateinit var fechaAPagar: String
    private lateinit var formaPago: String
    private lateinit var actividad: String
    private lateinit var tipoCliente: String
    private var monto: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pago)

        db = BDatos(this)

        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Detalle de Pago"
        btnAtras.setOnClickListener { finish() }

        val textNombre = findViewById<TextView>(R.id.textNombre)
        val textActividad = findViewById<TextView>(R.id.textActividad)
        val textMonto = findViewById<TextView>(R.id.textMonto)
        val textDescuento = findViewById<TextView>(R.id.textDescuento)
        val textTotal = findViewById<TextView>(R.id.textTotal)

        nombre = intent.getStringExtra("nombre") ?: ""
        apellido = intent.getStringExtra("apellido") ?: ""
        dni = intent.getStringExtra("dni") ?: ""
        fechaPago = intent.getStringExtra("fechaPago") ?: ""
        fechaAPagar = intent.getStringExtra("fechaAPagar") ?: ""
        formaPago = intent.getStringExtra("formaPago") ?: ""
        actividad = intent.getStringExtra("actividad") ?: ""
        tipoCliente = intent.getStringExtra("tipoCliente") ?: "No Socio"
        monto = intent.getFloatExtra("monto", 0f)

        textNombre.text = "$nombre $apellido"
        textActividad.text = actividad

        val precioBase = db.obtenerPrecioActividad(actividad)
        textMonto.text = String.format("%.2f", precioBase)

        val descuento = when (tipoCliente) {
            "Socio" -> (precioBase - monto).coerceAtLeast(0f)
            else -> 0f
        }
        textDescuento.text = String.format("-%.2f", descuento)
        textTotal.text = String.format("%.2f", monto)

        val btnIniciarPagar = findViewById<Button>(R.id.btnIniciarPagar)
        btnIniciarPagar.setOnClickListener {
            val idCliente = obtenerOCrearCliente()
            if (idCliente != null) {
                val idPago = registrarPago(idCliente)
                if (idPago != null) {
                    val intent = Intent(this, ComprobanteDePago::class.java).apply {
                        putExtra("id_pago", idPago) // ✅ Corregido
                        putExtra("nombre", nombre)
                        putExtra("apellido", apellido)
                        putExtra("dni", dni)
                        putExtra("actividad", actividad)
                        putExtra("fechaPago", fechaPago)
                        putExtra("fechaAPagar", fechaAPagar)
                        putExtra("formaPago", formaPago)
                        putExtra("monto", monto)
                        putExtra("tipoCliente", tipoCliente)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Error al registrar el pago.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Error al obtener/crear cliente.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun obtenerOCrearCliente(): Int? {
        val clienteExistente = db.buscarClientePorDNI(dni)
        return if (clienteExistente != null) {
            clienteExistente["id_cliente"].toString().toInt()
        } else {
            val insertQuery = """
                INSERT INTO Cliente (nombre, apellido, dni, email, telefono, cond_socio, apto_fisico)
                VALUES (?, ?, ?, '', '', ?, 1)
            """.trimIndent()
            val esSocio = if (tipoCliente == "Socio") 1 else 0
            val args = arrayOf(nombre, apellido, dni, esSocio.toString())
            val nuevoId = db.insertar(insertQuery, args)
            if (nuevoId != -1) nuevoId else null
        }
    }

    private fun registrarPago(idCliente: Int): Int? {
        val cuotas = when (formaPago) {
            "3 Cuotas" -> 3
            "6 Cuotas" -> 6
            else -> 1
        }

        val insertPagoQuery = """
            INSERT INTO Pagos (id_cliente, monto, cantidad_cuotas, medio_pago, fecha_pago, fecha_vencimiento)
            VALUES (?, ?, ?, ?, ?, ?)
        """.trimIndent()

        val args = arrayOf(
            idCliente.toString(),
            monto.toString(),
            cuotas.toString(),
            formaPago,
            fechaPago,
            fechaAPagar
        )

        val idPago = db.insertar(insertPagoQuery, args)
        if (idPago == -1) return null

        val idActividad = db.ejecutarConsultaSelect(
            "SELECT id_actividad FROM Actividades WHERE nombre_actividad = ?", arrayOf(actividad)
        ).firstOrNull()?.get("id_actividad")?.toString()?.toIntOrNull()

        if (idActividad != null) {
            val insertActividadPago = """
                INSERT INTO ActividadPago (id_actividad, id_pago) VALUES (?, ?)
            """.trimIndent()
            db.insertar(insertActividadPago, arrayOf(idActividad.toString(), idPago.toString()))
        }

        return idPago
    }
}
