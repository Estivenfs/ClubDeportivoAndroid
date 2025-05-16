package com.deportes.clubdeportivo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ConfiguracionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)

        val botonGuardar = findViewById<Button>(R.id.btnGuardarCambios)

        botonGuardar.setOnClickListener {

            val dialog = GuardadoExitosoFragment.newInstance()
            dialog.setOnVolverClickListener {
            }
            dialog.show(supportFragmentManager, GuardadoExitosoFragment.TAG)
        }
    }
}
