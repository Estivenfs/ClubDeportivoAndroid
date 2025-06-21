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
import android.widget.Button
import com.deportes.clubdeportivo.models.Cliente
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
        var socios : List<Cliente> = listOf()

        val btnTodos: Button = findViewById<Button>(R.id.btnTodos)
        val btnSinActividad: Button = findViewById<Button>(R.id.btnSinActividad)
        val btnProximos: Button = findViewById(R.id.btnProximos)
        btnTodos.setOnClickListener {
            socios = db.obtenerTodosSocios()
            mostrarSociosEnPantalla(socios)
            btnTodos.alpha = 1.0f
            btnSinActividad.alpha = 0.5f
            btnProximos.alpha = 0.5f
        }


        btnSinActividad.setOnClickListener {
            socios = db.obtenerVencidos()
            mostrarSociosEnPantalla(socios)
            btnTodos.alpha = 0.5f
            btnSinActividad.alpha = 1.0f
            btnProximos.alpha = 0.5f
        }


        btnProximos.setOnClickListener {
            socios = db.obtenerClientesConPagoMesAnterior()
            mostrarSociosEnPantalla(socios)
            btnTodos.alpha = 0.5f
            btnSinActividad.alpha = 0.5f
            btnProximos.alpha = 1.0f
        }

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Lista de Socios"

        btnAtras.setOnClickListener {
            finish()
        }


        // Lógica para mostrar la lista de socios
        socios = db.obtenerClientesConPagoMesAnterior()
        mostrarSociosEnPantalla(socios)


    }
    fun mostrarSociosEnPantalla(socios: List<Cliente>) {
        db = BDatos(this)
        recyclerView = findViewById(R.id.recyclerSocios)
        sinSociosText = TextView(this).apply {
            text = "No hay socios registrados"
            setTextColor(Color.WHITE)
            textSize = 18f
            gravity = Gravity.CENTER
        }



        if (socios.isEmpty()) {
            (recyclerView.parent as ViewGroup).addView(sinSociosText)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = SocioAdapter(socios)
        }

    }
}
