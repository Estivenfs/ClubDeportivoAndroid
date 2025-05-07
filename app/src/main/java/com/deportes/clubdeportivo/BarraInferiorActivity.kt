package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class BarraInferiorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barra_inferior)

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
            startActivity(Intent(this, ConsultasActivity::class.java))
        }

        btnPagos.setOnClickListener {
            //Nombre de pagos activity
            //startActivity(Intent(this, PagosActivity::class.java))
        }
    }
}
