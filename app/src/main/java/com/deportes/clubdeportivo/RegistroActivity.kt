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
        val volverButton = findViewById<Button>(R.id.buttonVolver)
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
            val registroExitosoDialog = RegistroExitosoFragment.newInstance(false)
            registroExitosoDialog.setOnVolverClickListener {
                finish()
            }

            // Mostrar el diálogo de registro exitoso primero
            registroExitosoDialog.show(
                supportFragmentManager,
                RegistroExitosoFragment.TAG
            )

            Handler(Looper.getMainLooper()).postDelayed({
                // Ocultar el diálogo de registro exitoso después de 1 segundo
                registroExitosoDialog.dismiss()


                // Mostrar el diálogo de carga después de ocultar el registro exitoso
                val cargandoDialog = CargandoFragment.newInstance()
                cargandoDialog.show(
                    supportFragmentManager,
                    CargandoFragment.TAG
                )

                Handler(Looper.getMainLooper()).postDelayed({
                    // Ocultar el diálogo de carga después de 3 segundos
                    val cargandoDialog =
                        supportFragmentManager.findFragmentByTag(CargandoFragment.TAG) as? CargandoFragment
                    cargandoDialog?.dismiss()
                    startActivity(Intent(this, MenuPrincipalActivity::class.java))
                }, 3000)
            }, 1000)
        }
    }
}