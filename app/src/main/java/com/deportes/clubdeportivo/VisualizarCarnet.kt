package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VisualizarCarnet : AppCompatActivity() {

    private var mostrandoFrente = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_carnet)

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Carnet"

        // Capturamos elementos de la vista
        val textViewIdCliente = findViewById<TextView>(R.id.TextViewIdCliente)
        val textViewNombreCliente = findViewById<TextView>(R.id.TextViewNombrecliente)
        val textViewApellidoCliente = findViewById<TextView>(R.id.TextViewApellidoCliente)


        // Recuperamos los datos del intent
        val idCliente = intent.getStringExtra("idCliente")
        val nombreCliente = intent.getStringExtra("nombreCliente")
        val apellidoCliente = intent.getStringExtra("apellidoCliente")


        // Mostramos los datos en la vista
        textViewIdCliente.text = "$idCliente"
        textViewNombreCliente.text = "$nombreCliente"
        textViewApellidoCliente.text = "$apellidoCliente"

        // Opcional: Mostrar un Toast si algún dato fundamental no llegó
        if (idCliente == null || nombreCliente == null || apellidoCliente == null) {
            Toast.makeText(this, "Algunos datos del carnet no se pudieron cargar.", Toast.LENGTH_LONG).show()
        }

        btnAtras.setOnClickListener {
            finish()
        }


        // --- Lógica para girar el carnet ---
        /*
        ## SE COMENTA TEMPORALMENTE
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
        }*/
    }
}