package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.Button

class PreciosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_precios)

        val barraSuperior: Toolbar = findViewById(R.id.barraSuperior)
        setSupportActionBar(barraSuperior)

        val btnVerCuotaMensual: Button = findViewById(R.id.btnCuotaMensual)
        btnVerCuotaMensual.setOnClickListener {
            val intent = Intent(this, CuotaMensualActivity::class.java)
            startActivity(intent)
        }
        val btnActividades: Button = findViewById(R.id.btnActividades)
        btnActividades.setOnClickListener {
            val intent = Intent(this, ActividadesActivity::class.java)
            startActivity(intent)
        }
        val btnDescuentos: Button = findViewById(R.id.btnDescuentos)
        btnDescuentos.setOnClickListener {
            val intent = Intent(this, DescuentosActivity::class.java)
            startActivity(intent)
        }

    }
}
