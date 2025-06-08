package com.deportes.clubdeportivo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.deportes.clubdeportivo.db.BDatos


class RegistroActivity : AppCompatActivity() {
    // Instancia de la base de datos
    private lateinit var db: BDatos

    // Referencias a los componentes de la vista para poder acceder a ellos fácilmente
    private lateinit var inputNombre : EditText
    private lateinit var inputDNI : EditText
    private lateinit var inputEmail : EditText
    private lateinit var inputContrasenia : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)


        // Obtenemos referencias a los elementos de la interfaz de usuario
        val volverButton = findViewById<Button>(R.id.buttonVolver)
        val iniciarSesionTextView: TextView = findViewById(R.id.textViewIniciarSesion)
        val btnCrearCuenta: Button = findViewById<Button>(R.id.buttonCrearCuenta)
        inputNombre = findViewById(R.id.editTextNombre)
        inputDNI = findViewById(R.id.editTextDni)
        inputEmail = findViewById(R.id.editTextMail)
        inputContrasenia = findViewById(R.id.editTextPassword)


        // Importación y lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Registro"

        btnAtras.setOnClickListener {
            finish()
        }

        // Cambiar a la pantalla de inicio de sesion
        iniciarSesionTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Logica del botón crear cuenta
        btnCrearCuenta.setOnClickListener {
            val id = registrarCliente()
            if (id == -1) {
                // Mostrar un mensaje de error si el registro falla
                Toast.makeText(this, "Registro fallido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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
                    val newIntent = Intent(this, MenuPrincipalActivity::class.java).apply {
                        putExtra("idUsuario", id.toString())
                        putExtra("nombreUsuario", inputNombre.text.toString())
                    }
                    startActivity(newIntent)
                }, 3000)
            }, 1000)
        }
    }

    private fun registrarCliente() : Int{
        val nombre = inputNombre.text.toString().trim()
        val dni = inputDNI.text.toString().trim()
        val email = inputEmail.text.toString().trim()
        val contrasenia = inputContrasenia.text.toString().trim()
        if (nombre.isEmpty() || dni.isEmpty() || email.isEmpty() || contrasenia.isEmpty()) {
            return -1
        }

        db = BDatos(this)
        val query = "INSERT INTO Usuario (nombre, dni, email, clave) VALUES (?, ?, ?, ?)"
        val args = arrayOf(nombre, dni, email, contrasenia)

        return db.insertar(query, args)


    }
}