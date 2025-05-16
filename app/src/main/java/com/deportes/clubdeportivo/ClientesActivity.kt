package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.ImageView
import android.widget.LinearLayout


class ClientesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes)


        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Clientes"

        btnAtras.setOnClickListener {
            finish()
        }

        val btnMenu = findViewById<LinearLayout>(R.id.btnMenu)
        val btnConsultas = findViewById<LinearLayout>(R.id.btnConsultas)
        val btnPagos = findViewById<LinearLayout>(R.id.btnPagos)
        val btnRegistrarCliente = findViewById<LinearLayout>(R.id.btnNuevoCliente)

        btnMenu.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
        }

        btnConsultas.setOnClickListener {
            startActivity(Intent(this, ConsultasActivity::class.java))
        }

        btnPagos.setOnClickListener {

        }

        btnRegistrarCliente.setOnClickListener {
            val registroExitosoDialog =
                RegistroExitosoFragment.newInstance() // Usar el nuevo nombre de la clase
            registroExitosoDialog.setOnVolverClickListener {
            }
            registroExitosoDialog.show(
                supportFragmentManager,
                CambioExitosoFragment.TAG
            )
        }

        val buttonAtras: ImageView = findViewById(R.id.ImageViewBtnAtras)

        buttonAtras.setOnClickListener {
            startActivity(Intent(this, ActualizacionDatosActivity::class.java))
        }
    }
}