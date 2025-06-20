// PagosDetallesSocioActivity.kt
package com.deportes.clubdeportivo

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
        val dniCliente = intent.getStringExtra("dni") ?: ""

        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Detalles de Pago (Socio)"
        btnAtras.setOnClickListener { finish() }

        val inputNombre: EditText = findViewById(R.id.inputNombre)
        val inputApellido: EditText = findViewById(R.id.inputApellido)
        val inputDNI: EditText = findViewById(R.id.inputDNI)
        val inputFechaPago: TextView = findViewById(R.id.inputFechaPago)
        val textMeses = findViewById<TextView>(R.id.textMesesSuscripcionSeleccionada)
        val textCuota = findViewById<TextView>(R.id.textTipoCuotaSeleccionada)
        val textPago = findViewById<TextView>(R.id.textOpcionesPagoSeleccionada)
        val btnCalcular: Button = findViewById(R.id.btnCalcularPagoSocio)

        val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        inputFechaPago.text = fechaHoy

        inputNombre.setText(nombreCliente.split(" ").firstOrNull() ?: "")
        inputApellido.setText(nombreCliente.split(" ").drop(1).joinToString(" "))
        inputDNI.setText(dniCliente)

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
}