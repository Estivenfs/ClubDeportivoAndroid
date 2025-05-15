package com.deportes.clubdeportivo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NuevoClienteActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_cliente)


        // Importación y lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Registrar Cliente"

        btnAtras.setOnClickListener {
            finish()
        }
        // Importacion y lógica de barra inferior
        val btnMenu = findViewById<LinearLayout>(R.id.btnMenu)
        val btnConsultas = findViewById<LinearLayout>(R.id.btnConsultas)
        val btnPagos = findViewById<LinearLayout>(R.id.btnPagos)
        val btnClientes = findViewById<LinearLayout>(R.id.btnNuevoCliente)

        // Asignar este valor a la pantalla en la que se encuentre
        btnClientes.alpha = 0.5f // Establece la transparencia al 50%
        btnClientes.isEnabled = false // Opcionalmente, desactiva los clics

        btnMenu.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
        }

        btnConsultas.setOnClickListener {
            startActivity(Intent(this, ConsultasActivity::class.java))
        }

        btnPagos.setOnClickListener {
            startActivity(Intent(this, PagosBusquedaActivity::class.java))
        }

        btnClientes.setOnClickListener {
            // Opción deshabilitada en esta pantalla
            // startActivity(Intent(this, NuevoClienteActivity::class.java))
        }


        val btnRegistrarCliente: Button = findViewById<Button>(R.id.btnRegistrarCliente)


        btnRegistrarCliente.setOnClickListener {
            val registroExitosoDialog =
                RegistroExitosoFragment.newInstance() // Usar el nuevo nombre de la clase
            registroExitosoDialog.setOnVolverClickListener {
                // ... lógica al volver ...
            }
            registroExitosoDialog.show(
                supportFragmentManager,
                CambioExitosoFragment.TAG
            ) // Usar el nuevo TAG
        }
    }

}
