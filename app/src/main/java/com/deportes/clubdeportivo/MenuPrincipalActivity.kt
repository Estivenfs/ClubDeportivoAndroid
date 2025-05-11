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
            val btnRegistrarActividad = findViewById<Button>(R.id.btnRegistrarActividad)
            val btnConsultas = findViewById<Button>(R.id.btnConsultas)
            val btnPagos = findViewById<Button>(R.id.btnPagos)
            val btnGestionarClientes = findViewById<Button>(R.id.btnGestionClientes)

            btnNuevoCliente.setOnClickListener {
                startActivity(Intent(this, NuevoClienteActivity::class.java))
            }

            btnRegistrarActividad.setOnClickListener {
                startActivity(Intent(this, RegistroActividadActivity::class.java))
            }

            btnConsultas.setOnClickListener {
                startActivity(Intent(this, ConsultasActivity::class.java))
            }

            btnPagos.setOnClickListener {
                startActivity(Intent(this, PagosBusquedaActivity::class.java))
            }

            btnGestionarClientes.setOnClickListener {
                startActivity(Intent(this, ClientesActivity::class.java))
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
