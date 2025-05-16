package com.deportes.clubdeportivo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.content.Intent
import android.widget.Button
import android.widget.ImageView


class RegistroActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)


        // Obtenemos referencias a los elementos de la interfaz de usuario
        val iniciarSesionTextView: TextView = findViewById(R.id.textViewIniciarSesion)
        val btnCrearCuenta: Button = findViewById<Button>(R.id.buttonCrearCuenta)


        // Importación y lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Registro"

        btnAtras.setOnClickListener {
            finish()
        }

        // Logica
        iniciarSesionTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Logica del botón crear cuenta
        btnCrearCuenta.setOnClickListener {
            val registroExitosoDialog =
                RegistroExitosoFragment.newInstance()
            registroExitosoDialog.setOnVolverClickListener {
                // ... lógica al volver ...
            }
            registroExitosoDialog.show(
                supportFragmentManager,
                RegistroExitosoFragment.TAG
            ) // Usar el nuevo TAG
        }
    }
}