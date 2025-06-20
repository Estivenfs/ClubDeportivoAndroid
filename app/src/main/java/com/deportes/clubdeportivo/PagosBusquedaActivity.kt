/*package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.deportes.clubdeportivo.db.BDatos

class PagosBusquedaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_busqueda)

        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Búsqueda de Pagos"
        btnAtras.setOnClickListener { finish() }

        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val inputDNI = findViewById<EditText>(R.id.inputDNI)
        val textCoincidencia = findViewById<TextView>(R.id.textCoincidencia)

        btnBuscar.setOnClickListener {
            val dniIngresado = inputDNI.text.toString().trim()

            if (dniIngresado.isEmpty()) {
                Toast.makeText(this, "Ingrese un DNI", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = BDatos(this)
            val query = """
                SELECT id_cliente, nombre, apellido, cond_socio
                FROM Cliente
                WHERE dni = ?
            """.trimIndent()

            val resultado = db.ejecutarConsultaSelect(query, arrayOf(dniIngresado))

            if (resultado.isEmpty()) {
                textCoincidencia.text = "No se encontró ningún cliente con ese DNI"
                return@setOnClickListener
            }

            val cliente = resultado[0]
            val idCliente = cliente["id_cliente"].toString().toInt()
            val nombre = cliente["nombre"].toString()
            val apellido = cliente["apellido"].toString()
            val esSocio = cliente["cond_socio"].toString() == "1"

            textCoincidencia.text = "Coincidencia: ${if (esSocio) "SOCIO" else "NO SOCIO"}: $nombre $apellido"

            val intent = if (esSocio) {
                Intent(this, PagosDetallesSocioActivity::class.java)
            } else {
                Intent(this, PagosDetallesNoSocioActivity::class.java)
            }

            intent.putExtra("id_cliente", idCliente)
            intent.putExtra("nombre", nombre)
            intent.putExtra("apellido", apellido)
            intent.putExtra("dni", dniIngresado)
            startActivity(intent)
        }
    }
}*/

package com.deportes.clubdeportivo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import android.util.Log
import android.content.ActivityNotFoundException
import java.io.Serializable
import com.deportes.clubdeportivo.db.BDatos
import com.deportes.clubdeportivo.PagosDetallesSocioActivity
import com.deportes.clubdeportivo.PagosDetallesNoSocioActivity

class PagosBusquedaActivity : AppCompatActivity() {
    private lateinit var db: BDatos
    private lateinit var inputDNI: EditText
    private lateinit var textCoincidencia: TextView
    private val TAG = "PagosBusquedaActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_busqueda)
        db = BDatos(this)

        // Obtener referencias a las vistas
        inputDNI = findViewById(R.id.inputDNI)
        textCoincidencia = findViewById(R.id.textCoincidencia)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Busqueda de Pagos"

        btnAtras.setOnClickListener {
            finish()
        }

        btnBuscar.setOnClickListener {
            val dni = inputDNI.text.toString().trim()

            if (dni.isEmpty()) {
                showToast("Por favor ingrese un DNI")
                return@setOnClickListener
            }

            if (dni.length < 7 || dni.length > 8) {
                showToast("El DNI debe tener 7 u 8 dígitos")
                return@setOnClickListener
            }

            buscarCliente(dni)
        }
    }

    private fun buscarCliente(dni: String) {
        try {
            // Usamos el método específico que creamos en BDatos
            val cliente = db.buscarClientePorDNI(dni)

            if (cliente != null) {
                // Obtenemos los datos con seguridad
                val idCliente = cliente["id_cliente"] as? Int ?: -1
                val nombre = cliente["nombre"] as? String ?: ""
                val apellido = cliente["apellido"] as? String ?: ""

                val esSocio = when (val cond = cliente["cond_socio"]) {
                    is Boolean -> cond
                    is Number -> cond.toInt() != 0
                    is String -> cond.equals("true", ignoreCase = true) || cond == "1"
                    else -> false
                }

                if (idCliente == -1) {
                    Log.e(TAG, "ID de cliente inválido. Datos: $cliente")
                    showToast("Error en datos del cliente")
                    return
                }

                // Mostrar resultado
                val tipoCliente = if (esSocio) "SOCIO" else "NO SOCIO"
                textCoincidencia.text = "Coincidencia: $tipoCliente: $nombre $apellido"
                textCoincidencia.visibility = TextView.VISIBLE

                // Redirigir
                redirigirSegunTipoCliente(esSocio, idCliente, nombre, apellido, dni)
                textCoincidencia.text = ""
                textCoincidencia.visibility = TextView.GONE
            } else {
                textCoincidencia.text = "No se encontró cliente con DNI: $dni"
                showToast("Cliente no encontrado")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al buscar cliente", e)
            showToast("Error al realizar la búsqueda")
            textCoincidencia.text = "Error en la búsqueda"
        }
    }

    private fun redirigirSegunTipoCliente(
        esSocio: Boolean,
        idCliente: Int,
        nombre: String,
        apellido: String,
        dni: String
    ) {
        try {
            val intent = if (esSocio) {
                Intent(this, PagosDetallesSocioActivity::class.java)
            } else {
                Intent(this, PagosDetallesNoSocioActivity::class.java)
            }.apply {
                putExtra("id_cliente", idCliente)
                putExtra("nombre", nombre)
                putExtra("apellido", apellido)
                putExtra("dni", dni)
            }

            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Error: Actividad no encontrada. ¿Está en el AndroidManifest.xml?", e)
            showToast("Error al abrir la pantalla")

        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado", e)
            showToast("Error al abrir la pantalla")
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
            // Configuración adicional si es necesaria
        }.show()
    }
}

