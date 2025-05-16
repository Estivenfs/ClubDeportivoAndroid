package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CarnetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet)

        // Importación y lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Carnet"

        btnAtras.setOnClickListener {
            finish()
        }

        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        btnBuscar.setOnClickListener {
            // Lógica para manejar el clic en el botón "Buscar"
            // Cambiar a activity correspondiente
            startActivity(Intent(this, VisualizarCarnet::class.java))
        }

    }
}