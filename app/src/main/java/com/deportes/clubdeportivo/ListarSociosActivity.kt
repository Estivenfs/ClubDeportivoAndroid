package com.deportes.clubdeportivo

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deportes.clubdeportivo.db.BDatos
import android.view.Gravity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.min


class ListarSociosActivity : AppCompatActivity() {
    private lateinit var db: BDatos
    private lateinit var recyclerView: RecyclerView
    private lateinit var sinSociosText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_socios)

        db = BDatos(this)

        /*val btnProximos: Button = findViewById(R.id.btnProximos)
        btnProximos.setOnClickListener {
            val fechaBuscada = getFechaMesAnterior()
            val socios = db.obtenerClientesConPagoMesAnterior(fechaBuscada)
            mostrarSociosEnPantalla(socios)
        }*/

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Lista de Socios"

        btnAtras.setOnClickListener {
            finish()
        }


        // Lógica para mostrar la lista de socios
        db = BDatos(this)
        recyclerView = findViewById(R.id.recyclerSocios)
        sinSociosText = TextView(this).apply {
            text = "No hay socios registrados"
            setTextColor(Color.WHITE)
            textSize = 18f
            gravity = Gravity.CENTER
        }

        //val fechaBuscada = getFechaMesAnterior()
        val socios = db.obtenerClientesConPagoMesAnterior()

        if (socios.isEmpty()) {
            (recyclerView.parent as ViewGroup).addView(sinSociosText)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = SocioAdapter(socios)
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
}
