package com.deportes.clubdeportivo
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.deportes.clubdeportivo.R
import com.deportes.clubdeportivo.utils.StringManager


class MenuPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)
        val bundle = intent.extras
        val sharedPref = getSharedPreferences("miAppPrefs", MODE_PRIVATE)
        val idUsuario = bundle?.getString("idUsuario")
        val nombreUsuario = sharedPref.getString("nombreUsuario", "Bienvenído")

        // Configurar el saludo del usuario
        val welcomeUser = findViewById<TextView>(R.id.textViewSaludo)
        val nombreCapitalizado = StringManager().capitalizeWords(nombreUsuario.toString())

        welcomeUser.text = "Hola, $nombreCapitalizado"

        try {
            val btnNuevoCliente = findViewById<LinearLayout>(R.id.btnNuevoCliente)
            val btnRegistrarActividad = findViewById<LinearLayout>(R.id.btnRegistrarActividad)
            val btnConsultas = findViewById<LinearLayout>(R.id.btnConsultas)
            val btnPagos = findViewById<LinearLayout>(R.id.btnPagos)
            val btnGestionarClientes = findViewById<LinearLayout>(R.id.btnGestionClientes)
            val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)
            val btnConfiguracion = findViewById<ImageButton>(R.id.imageButton)

            btnNuevoCliente.setOnClickListener {
                startActivity(Intent(this, NuevoClienteActivity::class.java))
            }

            btnRegistrarActividad.setOnClickListener {
                startActivity(Intent(this, RegistroActividadActivity::class.java))
            }

            btnConsultas.setOnClickListener {
                startActivity(Intent(this, ConsultasActivity::class.java))
            }

            btnPagos.setOnClickListener {
                startActivity(Intent(this, PagosBusquedaActivity::class.java))
            }

            btnGestionarClientes.setOnClickListener {
                startActivity(Intent(this, ClientesActivity::class.java))
            }

            btnCerrarSesion.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }

            btnConfiguracion.setOnClickListener {
                startActivity(Intent(this, ConfiguracionActivity::class.java))
            }

        } catch (err: Exception) {
            err.printStackTrace()
        }
    }
}
