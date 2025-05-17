package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.view.View

class DetallePagoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pago)

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Detalle de Pago"

        btnAtras.setOnClickListener {
            finish()
        }
        val btnIniciarPagar = findViewById<Button>(R.id.btnIniciarPagar)
        btnIniciarPagar.setOnClickListener {
            startActivity(Intent(this, ComprobanteDePago::class.java))
        }

        val btnCancelar = findViewById<Button>(R.id.btnCancelar)
        btnCancelar.setOnClickListener {
            startActivity(Intent(this, PagosDetallesSocioActivity::class.java))
        }
    }
}