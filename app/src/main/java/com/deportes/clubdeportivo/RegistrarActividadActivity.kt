package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class RegistroActividadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_actividad)

        val btnRegistrarActividad = findViewById<Button>(R.id.btnRegistrarActividad)
        val btnModificarActividad = findViewById<Button>(R.id.btnModificarActividad)
        val btnEliminarActividad = findViewById<Button>(R.id.btnEliminarActividad)


        btnRegistrarActividad.setOnClickListener {
            // Logica para registrar actividad
        }

        btnModificarActividad.setOnClickListener {
            startActivity(Intent(this, ModificarActividadActivity::class.java))
        }

    }
}
