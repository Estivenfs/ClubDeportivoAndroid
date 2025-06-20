// PagosDetallesNoSocioActivity.kt
package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.deportes.clubdeportivo.db.BDatos
import java.text.SimpleDateFormat
import java.util.*

class PagosDetallesNoSocioActivity : AppCompatActivity() {
    private lateinit var db: BDatos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_detalles_no_socio)

        db = BDatos(this)

        val idCliente = intent.getIntExtra("id_cliente", -1)
        val nombreCliente = intent.getStringExtra("nombre") ?: ""
        val dniCliente = intent.getStringExtra("dni") ?: ""

        val inputNombre: EditText = findViewById(R.id.inputNombre)
        val inputApellido: EditText = findViewById(R.id.inputApellido)
        val inputDNI: EditText = findViewById(R.id.inputDNI)
        val inputFechaPago: TextView = findViewById(R.id.inputFechaPago)
        val inputFechaAPagar: TextView = findViewById(R.id.inputFechaAPagar)
        val textActividad: TextView = findViewById(R.id.textActividadesSeleccionada)
        val textPago: TextView = findViewById(R.id.textOpcionesPagoSeleccionada)
        val btnCalcular: Button = findViewById(R.id.btnCalcularPagoNoSocio)

        val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        inputFechaPago.text = fechaHoy
        inputFechaAPagar.text = fechaHoy

        inputNombre.setText(nombreCliente.split(" ").firstOrNull() ?: "")
        inputApellido.setText(nombreCliente.split(" ").drop(1).joinToString(" "))
        inputDNI.setText(dniCliente)

        btnCalcular.setOnClickListener {
            val actividadNombre = textActividad.text.toString()
            val medioPago = textPago.text.toString()
            val fechaPago = inputFechaPago.text.toString()

            if (actividadNombre.isBlank() || medioPago.isBlank()) {
                Toast.makeText(this, "Selecciona actividad y método de pago.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = db.ejecutarConsultaSelect(
                "SELECT id_actividad, precio_actividad FROM Actividades WHERE nombre_actividad = ?",
                arrayOf(actividadNombre)
            )

            if (result.isEmpty()) {
                Toast.makeText(this, "Actividad no encontrada.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val idActividad = result[0]["id_actividad"].toString().toInt()
            var monto = result[0]["precio_actividad"].toString().toDouble()

            // Ajustes por forma de pago
            if (medioPago == "3 Cuotas") monto *= 1.05
            if (medioPago == "6 Cuotas") monto *= 1.10

            val insertPago = """
                INSERT INTO Pagos (id_cliente, monto, cantidad_cuotas, medio_pago, fecha_pago)
                VALUES (?, ?, ?, ?, ?)
            """.trimIndent()

            val idPago = db.insertar(insertPago, arrayOf(idCliente.toString(), monto.toString(), "1", medioPago, fechaPago))

            if (idPago != -1) {
                val insertActividadPago = """
                    INSERT INTO ActividadPago (id_actividad, id_pago)
                    VALUES (?, ?)
                """.trimIndent()
                db.insertar(insertActividadPago, arrayOf(idActividad.toString(), idPago.toString()))

                val intent = Intent(this, ComprobanteDePago::class.java)
                intent.putExtra("id_pago", idPago)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error al registrar el pago", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
