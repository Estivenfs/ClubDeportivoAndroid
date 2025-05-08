package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ConsultasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultas)

        // Barra inferior
        val btnMenu = findViewById<LinearLayout>(R.id.btnMenu)
        val btnNuevoCliente = findViewById<LinearLayout>(R.id.btnNuevoCliente)
        val btnPagos = findViewById<LinearLayout>(R.id.btnPagos)

        btnMenu.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
        }

        btnNuevoCliente.setOnClickListener {
            startActivity(Intent(this, NuevoClienteActivity::class.java))
        }


        btnPagos.setOnClickListener {
            //Nombre de pagos activity
            //startActivity(Intent(this, PagosActivity::class.java))
        }
        val btnListarSocios = findViewById<Button>(R.id.btnListarSocios)
        val btnCarnet = findViewById<Button>(R.id.btnCarnet)
        val btnPrecios = findViewById<Button>(R.id.btnPrecios)

        btnListarSocios.setOnClickListener {
        }

        btnCarnet.setOnClickListener {
        }

        btnPrecios.setOnClickListener {
        }
    }
}
