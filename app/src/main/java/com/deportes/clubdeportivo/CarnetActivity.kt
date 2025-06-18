package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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

        // Lógica btn Atras
        btnAtras.setOnClickListener {
            finish()
        }

        // Lógica del boton Buscar
        btnBuscar.setOnClickListener {
            val dniIngresado = inputDNI.text.toString().trim()
            // Lógica para manejar el clic en el botón "Buscar"
            // Cambiar a activity correspondiente
            if (dniIngresado == "1234") {
                Toast.makeText(this, "DNI INGRESADO CORRECTAMENTE.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, VisualizarCarnet::class.java).apply {
                    putExtra("dniCliente", dniIngresado)
                    putExtra("nombreClienteSimulado", "Cliente Simulado DNI 1234")
                }
                startActivity(intent)
                finish()
            } else {
                textCoincidencia.text = "No se encontraron coincidencias. Verifique el DNI ingresado."
                textCoincidencia.visibility = View.VISIBLE
            }
        }
    }
}