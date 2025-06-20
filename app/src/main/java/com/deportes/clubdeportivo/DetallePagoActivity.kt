package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.view.View
import android.widget.Toast
import com.deportes.clubdeportivo.db.BDatos

class DetallePagoActivity : AppCompatActivity() {
    private lateinit var db: BDatos
    private var idCliente: Int? = null
    private lateinit var nombre: String
    private lateinit var apellido: String
    private lateinit var dni: String
    private lateinit var fechaPago: String
    private lateinit var fechaInicio: String
    private lateinit var fechaExpiracion: String
    var mesesSuscripcionSeleccionada: Int = 0
    private lateinit var tipoCuotaSeleccionada: String
    private lateinit var opcionesPagoSeleccionada: String
    private lateinit var monto: String
    private lateinit var montoBase: String
    private lateinit var actividad: String
    private lateinit var tipoCliente: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pago)
        db = BDatos(this)
        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Detalle de Pago"

        btnAtras.setOnClickListener {
            finish()
        }

        // Obtengo los elementos del layout
        val textNombre = findViewById<TextView>(R.id.textNombre)
        val textActividad = findViewById<TextView>(R.id.textActividad)
        val textMonto = findViewById<TextView>(R.id.textMonto)
        val textDescuento = findViewById<TextView>(R.id.textDescuento)
        val textTotal = findViewById<TextView>(R.id.textTotal)



        // Obtener datos del intent
        idCliente = intent.getIntExtra("idCliente", -1)
        nombre = intent.getStringExtra("nombre") ?: ""
        apellido = intent.getStringExtra("apellido") ?: ""
        dni = intent.getStringExtra("dni") ?: ""
        fechaPago = intent.getStringExtra("fechaPago") ?: ""
        fechaInicio = intent.getStringExtra("fechaInicio") ?: ""
        fechaExpiracion = intent.getStringExtra("fechaExpiracion") ?: ""
        mesesSuscripcionSeleccionada = intent.getIntExtra("mesesSuscripcion",0)
        tipoCuotaSeleccionada = intent.getStringExtra("tipoCuota") ?: ""
        opcionesPagoSeleccionada = intent.getStringExtra("opcionPago") ?: ""
        monto = intent.getStringExtra("monto") ?: ""
        montoBase = intent.getStringExtra("montoBase") ?: ""
        actividad = intent.getStringExtra("actividad") ?: ""
        tipoCliente = intent.getStringExtra("tipoCliente") ?: ""

        // Mostrar datos en los TextViews
        textNombre.text = "$nombre $apellido"
        textActividad.text = actividad
        textMonto.text = montoBase
        val descuento = montoBase.toDouble() - monto.toDouble()
        textDescuento.text = "-$descuento"
        textTotal.text = monto


        val btnIniciarPagar = findViewById<Button>(R.id.btnIniciarPagar)
        btnIniciarPagar.setOnClickListener {
            val idPago = guardarPago()
            if(idPago != 0){
                val intent = Intent(this, DescargarComprobanteActivity::class.java).apply {
                    putExtra("idCliente", idCliente)
                    putExtra("nombre", nombre)
                    putExtra("apellido", apellido)
                    putExtra("dni", dni)
                    putExtra("idPago", idPago)
                    putExtra("tipoCliente", tipoCliente)
                    putExtra("actividad", actividad)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Error al registrar el pago.", Toast.LENGTH_LONG).show()
            }
        }

        val btnCancelar = findViewById<Button>(R.id.btnCancelar)
        btnCancelar.setOnClickListener {
            startActivity(Intent(this, PagosDetallesSocioActivity::class.java))
        }
    }

    private fun guardarPago() : Int {
        // 2. Insertar el pago
        if (idCliente != null && idCliente != -1) {
            // Columnas: id_cliente, monto, cantidad_cuotas, medio_pago, fecha_pago
            val insertPagoQuery = """
                    INSERT INTO Pagos (id_cliente, monto, cantidad_cuotas, medio_pago, fecha_pago,fecha_vencimiento)
                    VALUES (?, ?, ?, ?, ?, ?)
                """.trimIndent()
            var cantidadDePagos: Int = 1
            when (opcionesPagoSeleccionada) {
                "6 Cuotas" -> cantidadDePagos = 6
                "3 Cuotas (-5%)" -> cantidadDePagos = 3
                "Efectivo (-10%)" -> cantidadDePagos = 1
            }
            val insertPagoArgs = arrayOf(
                idCliente.toString(),
                monto.toString(),
                cantidadDePagos.toString(),
                actividad,
                fechaPago,
                fechaExpiracion
            )
            var idPago = -1
            try {
                idPago = db.insertar(insertPagoQuery, insertPagoArgs)
            } catch (e: Exception) {
                Toast.makeText(this, "Error al registrar el pago: ${e.message}", Toast.LENGTH_LONG).show()
            }


            if (idPago != -1) {
                Toast.makeText(this, "Pago registrado exitosamente. ID: $idPago", Toast.LENGTH_LONG).show()
                return idPago
                // Si tienes una actividad a la que pasarle los detalles de pago, puedes hacerlo aquí
                // val intent = Intent(this, DetallePagoActivity::class.java)
                // intent.putExtra("pago_id", idPago)
                // startActivity(intent)
            } else {
                Toast.makeText(this, "Error al registrar el pago.", Toast.LENGTH_LONG).show()
                return 0
            }
        } else {
            Toast.makeText(this, "No se pudo obtener/crear el ID del cliente.", Toast.LENGTH_LONG).show()
            return 0
        }


    }
}