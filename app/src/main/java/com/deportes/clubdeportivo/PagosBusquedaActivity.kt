package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.deportes.clubdeportivo.db.BDatos

class PagosBusquedaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_busqueda)

        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Búsqueda de Pagos"
        btnAtras.setOnClickListener { finish() }

        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val inputDNI = findViewById<EditText>(R.id.inputDNI)
        val textCoincidencia = findViewById<TextView>(R.id.textCoincidencia)

        btnBuscar.setOnClickListener {
            val dniIngresado = inputDNI.text.toString().trim()

            if (dniIngresado.isEmpty()) {
                Toast.makeText(this, "Ingrese un DNI", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = BDatos(this)
            val query = """
                SELECT id_cliente, nombre, apellido, cond_socio 
                FROM Cliente 
                WHERE dni = ?
            """.trimIndent()

            val resultado = db.ejecutarConsultaSelect(query, arrayOf(dniIngresado))

            if (resultado.isEmpty()) {
                textCoincidencia.text = "No se encontró ningún cliente con ese DNI"
                return@setOnClickListener
            }

            val cliente = resultado[0]
            val idCliente = cliente["id_cliente"].toString().toInt()
            val nombre = cliente["nombre"].toString()
            val apellido = cliente["apellido"].toString()
            val esSocio = cliente["cond_socio"].toString() == "1"

            textCoincidencia.text = "Coincidencia: ${if (esSocio) "SOCIO" else "NO SOCIO"}: $nombre $apellido"

            val intent = if (esSocio) {
                Intent(this, PagosDetallesSocioActivity::class.java)
            } else {
                Intent(this, PagosDetallesNoSocioActivity::class.java)
            }

            intent.putExtra("id_cliente", idCliente)
            intent.putExtra("nombre", "$nombre $apellido")
            intent.putExtra("dni", dniIngresado)
            startActivity(intent)
        }
    }
}
