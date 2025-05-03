package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class ConsultasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultas)

        // Barra inferior
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

        val btnListarSocios = findViewById<Button>(R.id.btnListarSocios)
        val btnCarnet = findViewById<Button>(R.id.btnCarnet)
        val btnPrecios = findViewById<Button>(R.id.btnPrecios)

        btnListarSocios.setOnClickListener {
        }

        btnCarnet.setOnClickListener {
        }

        btnPrecios.setOnClickListener {
        }
    }
}
