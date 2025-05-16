package com.deportes.clubdeportivo

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DescuentosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_descuentos)

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Descuentos"

        btnAtras.setOnClickListener {
            finish()
        }
    }
}
