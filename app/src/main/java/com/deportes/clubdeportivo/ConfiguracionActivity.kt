package com.deportes.clubdeportivo

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfiguracionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)
        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Configuración"

        btnAtras.setOnClickListener {
            finish()
        }
        val botonGuardar = findViewById<Button>(R.id.btnGuardarCambios)

        botonGuardar.setOnClickListener {

            val dialog = GuardadoExitosoFragment.newInstance()
            dialog.setOnVolverClickListener {
            }
            dialog.show(supportFragmentManager, GuardadoExitosoFragment.TAG)
        }
    }
}
