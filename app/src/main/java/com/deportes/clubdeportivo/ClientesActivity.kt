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


       /* val btnRegistrarCliente = findViewById<LinearLayout>(R.id.btnNuevoCliente)



        btnRegistrarCliente.setOnClickListener {
            val registroExitosoDialog = RegistroExitosoFragment.newInstance()
            registroExitosoDialog.setOnVolverClickListener {
                // lógica al volver si querés
            }
            registroExitosoDialog.show(
                supportFragmentManager,
                RegistroExitosoFragment.TAG
            )
        }*/

       val buttonAtras: ImageView = findViewById(R.id.ImageViewBtnAtras)

        buttonAtras.setOnClickListener {
            startActivity(Intent(this, ActualizacionDatosActivity::class.java))
        }
    }
}