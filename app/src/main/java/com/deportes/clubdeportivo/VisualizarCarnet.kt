package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VisualizarCarnet : AppCompatActivity() {

    private var mostrandoFrente = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_carnet)

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Clientes"

        btnAtras.setOnClickListener {
            finish()
        }

        // Lógica de la barra inferior
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
            startActivity(Intent(this, PagosBusquedaActivity::class.java))
        }

        btnRegistrarCliente.setOnClickListener {
            startActivity(Intent(this, NuevoClienteActivity::class.java))
        }

        // --- Lógica para girar el carnet ---
        val carnet = findViewById<ImageView>(R.id.carnet)
        val girarCarnet = findViewById<ImageButton>(R.id.girarcarnet)

        girarCarnet.setOnClickListener {
            carnet.animate()
                .rotationY(90f)
                .setDuration(150)
                .withEndAction {
                    // Cambia la imagen cuando está "girando"
                    if (mostrandoFrente) {
                        carnet.setImageResource(R.drawable.carnet_back)
                    } else {
                        carnet.setImageResource(R.drawable.carnet_front)
                    }
                    mostrandoFrente = !mostrandoFrente

                    // Continua la animación hacia el frente
                    carnet.rotationY = -90f
                    carnet.animate().rotationY(0f).setDuration(150).start()
                }
                .start()
        }
    }
}