package com.deportes.clubdeportivo

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.deportes.clubdeportivo.db.BDatos
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class ListarSociosActivity : AppCompatActivity() {
    private lateinit var db: BDatos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_socios)

        db = BDatos(this)

        val btnProximos: Button = findViewById(R.id.btnProximos)
        btnProximos.setOnClickListener {
            val fechaBuscada = getFechaMesAnterior()
            val socios = db.obtenerClientesConPagoMesAnterior(fechaBuscada)
            mostrarSociosEnPantalla(socios)
        }

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Lista de Socios"

        btnAtras.setOnClickListener {
            finish()
        }
    }

    private fun getFechaMesAnterior(): String {
        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Ajustalo si usás otro formato
        val calendar = Calendar.getInstance()
        val dia = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.add(Calendar.MONTH, -1)

        val maxDia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar.set(Calendar.DAY_OF_MONTH, min(dia, maxDia))

        return formato.format(calendar.time)
    }

    private fun mostrarSociosEnPantalla(socios: List<Map<String, Any>>) {
        val contenedor = findViewById<LinearLayout>(R.id.listaSociosContainer)
        contenedor.removeAllViews()

        for (socio in socios) {
            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setBackgroundColor(Color.argb(26, 255, 255, 255)) // 0x1AFFFFFF como Color.argb
                setPadding(8, 8, 8, 8)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    130 // altura aproximada como en tu XML
                ).apply {
                    bottomMargin = 8
                }
                gravity = android.view.Gravity.CENTER_VERTICAL
            }

            val idView = TextView(this).apply {
                text = socio["id_cliente"].toString()
                setTextColor(Color.WHITE)
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val nombreView = TextView(this).apply {
                text = "${socio["nombre"]} ${socio["apellido"]}"
                setTextColor(Color.WHITE)
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f)
            }

            val dniView = TextView(this).apply {
                text = socio["dni"].toString()
                setTextColor(Color.WHITE)
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f)
            }

            layout.addView(idView)
            layout.addView(nombreView)
            layout.addView(dniView)

            contenedor.addView(layout)
        }
    }
}
