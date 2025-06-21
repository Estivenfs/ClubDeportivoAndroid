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

        val db = BDatos(this)

        btnIniciarSesion.setOnClickListener {
            val email = inputEmail.text.toString().trim()
            val password = inputPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val query = "SELECT id, nombre FROM Usuario WHERE email = ? AND clave = ?"
            val resultado = db.ejecutarConsultaSelect(query, arrayOf(email, password))

            if (resultado.isNotEmpty()) {
                val idUsuario = resultado[0]["id"] as Int
                val nombreUsuario = resultado[0]["nombre"] as String

                val intent = Intent(this, MenuPrincipalActivity::class.java).apply {
                    putExtra("idUsuario", idUsuario.toString())
                    putExtra("nombreUsuario", nombreUsuario)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        registrateTextView.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}
