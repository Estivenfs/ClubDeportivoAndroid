package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deportes.clubdeportivo.db.BDatos

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion)
        val registrateTextView: TextView = findViewById(R.id.textViewRegistrate)
        val inputEmail = findViewById<TextView>(R.id.etEmail)
        val inputPassword = findViewById<TextView>(R.id.etPassword)

        // Instancia de la base de datos (la tabla Usuario debe existir)
        val db = BDatos(this)

        btnIniciarSesion.setOnClickListener {
            val email = inputEmail.text.toString().trim()
            val password = inputPassword.text.toString().trim()
            startActivity(Intent(this, MenuPrincipalActivity::class.java))

            /*

                DESCOMENTAR ANTES DE SUBIR A GITHUB
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Consulta SQL segura
            val query = "SELECT id, nombre FROM Usuario WHERE email = ? AND clave = ?"
            val resultado = db.ejecutarConsultaSelect(query, arrayOf(email, password))

            if (resultado.isNotEmpty()) {
                // Login exitoso
                //Obtengo el id del usuario


                val idUsuario = resultado[0]["id"] as Int
                val nombreUsuario = resultado[0]["nombre"] as String

                val intent = Intent(this, MenuPrincipalActivity::class.java).apply {
                    putExtra("idUsuario", idUsuario.toString())
                    putExtra("nombreUsuario", nombreUsuario)
                }
                startActivity(intent)
                finish() // evita volver al login al presionar "atrás"
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }

            */
        }

        registrateTextView.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}
