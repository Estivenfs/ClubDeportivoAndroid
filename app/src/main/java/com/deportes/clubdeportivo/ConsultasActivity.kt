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

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Consultas"

        btnAtras.setOnClickListener {
            finish()
        }
        val btnListarSocios = findViewById<Button>(R.id.btnListarSocios)
        val btnCarnet = findViewById<Button>(R.id.btnCarnet)
        val btnPrecios = findViewById<Button>(R.id.btnPrecios)

        btnListarSocios.setOnClickListener {
            val intent = Intent(this, ListarSociosActivity::class.java)
            startActivity(intent)
        }

        btnCarnet.setOnClickListener {
            val intent = Intent(this, CarnetActivity::class.java)
            startActivity(intent)}

        btnPrecios.setOnClickListener {
        val intent = Intent(this, PreciosActivity::class.java)
        startActivity(intent)}
    }
}
