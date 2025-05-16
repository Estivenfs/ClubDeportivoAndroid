package com.deportes.clubdeportivo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.content.Intent
import android.os.Handler
import android.os.Looper
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
                finish()
            }
            registroExitosoDialog.show(
                supportFragmentManager,
                RegistroExitosoFragment.TAG
            ) // Usar el nuevo TAG

            // Vista de carga de pantalla
            val cargandoDialog = CargandoFragment.newInstance()
            cargandoDialog.show(supportFragmentManager, CargandoFragment.TAG)
            Handler(Looper.getMainLooper()).postDelayed({
                val cargandoDialog = supportFragmentManager.findFragmentByTag(CargandoFragment.TAG) as? CargandoFragment
                cargandoDialog?.dismiss()
                startActivity(Intent(this, MenuPrincipalActivity::class.java))
            }, 3000)
        }
    }
}