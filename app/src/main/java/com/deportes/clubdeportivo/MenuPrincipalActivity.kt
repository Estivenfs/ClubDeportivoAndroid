package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        try {
            val btnNuevoCliente = findViewById<Button>(R.id.btnNuevoCliente)
            val btnConsultas = findViewById<Button>(R.id.btnConsultas)
            val btnPagos = findViewById<Button>(R.id.btnPagos)

            btnNuevoCliente.setOnClickListener {
                startActivity(Intent(this, NuevoClienteActivity::class.java))
            }

            btnConsultas.setOnClickListener {
                startActivity(Intent(this, ConsultasActivity::class.java))
            }

            btnPagos.setOnClickListener {
                startActivity(Intent(this, PagosBusquedaActivity::class.java))
            }

        } catch (err: Exception) {
            err.printStackTrace()
        }

        val btnRegistrarActividad = findViewById<Button>(R.id.btnRegistrarActividad)
        btnRegistrarActividad.setOnClickListener {
            startActivity(Intent(this, RegistroActividadActivity::class.java))
        }
    }
}
