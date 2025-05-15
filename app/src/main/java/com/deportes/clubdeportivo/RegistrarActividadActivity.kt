package com.deportes.clubdeportivo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegistroActividadActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_actividad)

        val btnRegistrarActividad = findViewById<Button>(R.id.btnRegistrarActividad)
        val btnModificarActividad = findViewById<Button>(R.id.btnModificarActividad)
        val btnEliminarActividad = findViewById<Button>(R.id.btnEliminarActividad)

        // Importación y lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Registrar actividad"

        btnAtras.setOnClickListener {
            finish()
        }

        // Importacion y lógica de barra inferior
        val btnMenu = findViewById<LinearLayout>(R.id.btnMenu)
        val btnConsultas = findViewById<LinearLayout>(R.id.btnConsultas)
        val btnPagos = findViewById<LinearLayout>(R.id.btnPagos)
        val btnClientes = findViewById<LinearLayout>(R.id.btnNuevoCliente)

        // Asignar este valor a la pantalla en la que se encuentre
        // btnMenu.alpha = 0.5f // Establece la transparencia al 50%
        // btnMenu.isEnabled = false // Opcionalmente, desactiva los clics

        btnMenu.setOnClickListener {
            startActivity(Intent(this, MenuPrincipalActivity::class.java))
        }

        btnConsultas.setOnClickListener {
            startActivity(Intent(this, ConsultasActivity::class.java))
        }

        btnPagos.setOnClickListener {
            startActivity(Intent(this, PagosBusquedaActivity::class.java))
        }

        btnClientes.setOnClickListener {
            startActivity(Intent(this, NuevoClienteActivity::class.java))
        }

        // Lógica al registrar actividad
        btnRegistrarActividad.setOnClickListener {
            val registroExitosoDialog = RegistroExitosoFragment.newInstance()
            registroExitosoDialog.setOnVolverClickListener {
                // ... lógica al volver ...
            }
            registroExitosoDialog.show(
                supportFragmentManager, RegistroExitosoFragment.TAG
            ) // Usar el nuevo TAG
        }

        // Lógica al modificar actividad
        btnModificarActividad.setOnClickListener {
            startActivity(Intent(this, ModificarActividadActivity::class.java))
        }

        // Lógica al eliminar actividad
        btnEliminarActividad.setOnClickListener {
            val eliminarActividadDialog = EliminarActividadFragment.newInstance()
            eliminarActividadDialog.setOnVolverClickListener {
                // ... lógica al volver ...
            }
            eliminarActividadDialog.show(
                supportFragmentManager, EliminarActividadFragment.TAG
            ) // Usar el nuevo TAG
        }

    }
}
