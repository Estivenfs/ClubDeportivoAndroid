package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deportes.clubdeportivo.db.BDatos

class CarnetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet)

        // Importación y lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Carnet"

        // Captura de valores ingresados
        val inputDNI = findViewById<TextView>(R.id.inputDNI)
        val textCoincidencia = findViewById<TextView>(R.id.textCoincidencia)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)

        // Consulta SQL segura para buscar el DNI en BBDD
        val db = BDatos(this)

        // Lógica btn Atras
        btnAtras.setOnClickListener {
            finish()
        }

        // Lógica del boton Buscar
        btnBuscar.setOnClickListener {
            val dniIngresado = inputDNI.text.toString().trim()

            // Validamos el DNI vacío
            if (dniIngresado.isEmpty()) {
                textCoincidencia.text = "Por favor, ingresa el DNI de un cliente."
                textCoincidencia.visibility = View.VISIBLE
                inputDNI.text = ""
                return@setOnClickListener
            }

            // Ejecutamos la consulta en la base de datos
            val query = "SELECT id_cliente, nombre, apellido FROM Cliente WHERE dni = ?"
            val resultadoBD = db.ejecutarConsultaSelect(query, arrayOf(dniIngresado))

            // Procesamos el resultado obtenido
            if (resultadoBD.isNotEmpty()) {
                val idCliente = resultadoBD[0]["id_cliente"] as Int
                val nombreCliente = resultadoBD[0]["nombre"] as String
                val apellidoCliente = resultadoBD[0]["apellido"] as String
                val dniCliente = dniIngresado
                textCoincidencia.visibility = View.GONE

                val intent = Intent(this, VisualizarCarnet::class.java).apply {
                    putExtra("idCliente", idCliente.toString())
                    putExtra("nombreCliente", nombreCliente)
                    putExtra("apellidoCliente", apellidoCliente)
                    putExtra("dniCliente", dniCliente)
                }
                startActivity(intent)
                finish()
            } else {
                textCoincidencia.text = "Cliente con DNI $dniIngresado no encontrado."
                textCoincidencia.visibility = View.VISIBLE
                inputDNI.text = ""
            }
        }
    }
}