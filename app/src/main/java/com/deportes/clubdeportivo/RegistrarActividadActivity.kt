package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class RegistroActividadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_actividad)

        val btnMenu = findViewById<LinearLayout>(R.id.btnMenu)
        val btnNuevoCliente = findViewById<LinearLayout>(R.id.btnNuevoCliente)
        val btnConsultas = findViewById<LinearLayout>(R.id.btnConsultas)
        val btnPagos = findViewById<LinearLayout>(R.id.btnPagos)

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
