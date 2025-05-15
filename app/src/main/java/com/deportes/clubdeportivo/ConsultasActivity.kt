package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConsultasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultas)


        val btnListarSocios = findViewById<Button>(R.id.btnListarSocios)
        val btnCarnet = findViewById<Button>(R.id.btnCarnet)
        val btnPrecios = findViewById<Button>(R.id.btnPrecios)

        btnListarSocios.setOnClickListener {
        }

        btnCarnet.setOnClickListener {
        }

        btnPrecios.setOnClickListener {
            val intent = Intent(this, PreciosActivity::class.java)
            startActivity(intent)}

        // Importación y lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Consultas"

        btnAtras.setOnClickListener {
            finish()
        }

        // Importacion y lógica de barra inferior
        val btnMenu = findViewById<LinearLayout>(R.id.btnMenu)
        val btnConsultas = findViewById<LinearLayout>(R.id.btnConsultas)
        val btnPagos = findViewById<LinearLayout>(R.id.btnPagos)
        val btnClientes = findViewById<LinearLayout>(R.id.btnNuevoCliente)

        // Asignar este valor a la pantalla en la que se encuentre
        btnConsultas.alpha = 0.5f // Establece la transparencia al 50%
        btnConsultas.isEnabled = false // Opcionalmente, desactiva los clics

        btnMenu.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
        }

        btnConsultas.setOnClickListener {
        // Opción deshabilitada para esta sección
        // startActivity(Intent(this, ConsultasActivity::class.java))
        }

        btnPagos.setOnClickListener {
            startActivity(Intent(this, PagosBusquedaActivity::class.java))
        }

        btnClientes.setOnClickListener {
            startActivity(Intent(this, NuevoClienteActivity::class.java))
        }
    }
}
