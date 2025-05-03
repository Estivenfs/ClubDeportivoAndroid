package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class RegistroActividadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_actividad)

        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        val btnNuevoCliente = findViewById<ImageButton>(R.id.btnNuevoCliente)
        val btnConsultas = findViewById<ImageButton>(R.id.btnConsultas)
        val btnPagos = findViewById<ImageButton>(R.id.btnPagos)

        btnMenu.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
        }

        btnNuevoCliente.setOnClickListener {
            startActivity(Intent(this, NuevoClienteActivity::class.java))
        }

        btnConsultas.setOnClickListener {
        }

        btnPagos.setOnClickListener {
        }
    }
}
