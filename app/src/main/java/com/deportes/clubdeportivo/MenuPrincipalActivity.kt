package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        val btnNuevoCliente = findViewById<Button>(R.id.btnNuevoCliente)
        btnNuevoCliente.setOnClickListener {
            val intent = Intent(this, NuevoClienteActivity::class.java)
            startActivity(intent)
        }
        val btnRegistrarActividad = findViewById<Button>(R.id.btnRegistrarActividad)
        btnRegistrarActividad.setOnClickListener {
            startActivity(Intent(this, RegistroActividadActivity::class.java))
        }
        val btnConsultas = findViewById<Button>(R.id.btnConsultas)
        btnConsultas.setOnClickListener {
            startActivity(Intent(this, ConsultasActivity::class.java))
        }
    }
}
