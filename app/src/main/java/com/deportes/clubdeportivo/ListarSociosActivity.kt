package com.deportes.clubdeportivo

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deportes.clubdeportivo.db.BDatos
import com.deportes.clubdeportivo.models.Cliente
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.min

class ListarSociosActivity : AppCompatActivity() {
    private lateinit var db: BDatos
    private lateinit var recyclerView: RecyclerView
    private lateinit var sinSociosText: TextView
    private lateinit var adapter: SocioAdapter

    private lateinit var btnTodos: Button
    private lateinit var btnProximos: Button
    private lateinit var btnSinActividad: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_socios)

        db = BDatos(this)

        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Lista de Socios"

        btnAtras.setOnClickListener {
            finish()
        }

        // CORRECCIÓN: Inicializar recyclerView antes de usarlo
        recyclerView = findViewById(R.id.recyclerSocios)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SocioAdapter(mutableListOf())
        recyclerView.adapter = adapter

        sinSociosText = TextView(this).apply {
            text = "No hay socios registrados"
            setTextColor(Color.WHITE)
            textSize = 18f
            gravity = Gravity.CENTER
        }

        btnTodos = findViewById(R.id.btnTodos)
        btnProximos = findViewById(R.id.btnProximos)
        btnSinActividad = findViewById(R.id.btnSinActividad)

        // Cargar la lista inicial con todos los socios
        cargarTodos()

        btnTodos.setOnClickListener {
            cargarTodos()
            actualizarAlphaBotones(btnTodos)
        }

        btnProximos.setOnClickListener {
            cargarProximos()
            actualizarAlphaBotones(btnProximos)
        }

        btnSinActividad.setOnClickListener {
            cargarSinActividad()
            actualizarAlphaBotones(btnSinActividad)
        }
    }

    private fun cargarTodos() {
        val socios = db.obtenerSocios()
        mostrarSocios(socios)
    }

    private fun cargarProximos() {
        val fechaBuscada = getFechaMesAnterior()
        val socios = db.obtenerClientesConPagoMesAnterior(fechaBuscada)
        mostrarSocios(socios)
    }

    private fun cargarSinActividad() {
        val sociosSinActividad = db.obtenerSociosSinActividad()
        mostrarSocios(sociosSinActividad)
    }

    private fun mostrarSocios(socios: List<Cliente>) {
        val parent = recyclerView.parent as ViewGroup

        if (socios.isEmpty()) {
            if (sinSociosText.parent == null) {
                parent.addView(sinSociosText)
            }
            recyclerView.visibility = android.view.View.GONE
        } else {
            if (sinSociosText.parent != null) {
                parent.removeView(sinSociosText)
            }
            recyclerView.visibility = android.view.View.VISIBLE
            adapter.actualizarLista(socios)
        }
    }

    private fun actualizarAlphaBotones(botonActivo: Button) {
        btnTodos.alpha = if (botonActivo == btnTodos) 1f else 0.5f
        btnProximos.alpha = if (botonActivo == btnProximos) 1f else 0.5f
        btnSinActividad.alpha = if (botonActivo == btnSinActividad) 1f else 0.5f
    }

    private fun getFechaMesAnterior(): String {
        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val dia = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.add(Calendar.MONTH, -1)

        val maxDia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar.set(Calendar.DAY_OF_MONTH, min(dia, maxDia))

        return formato.format(calendar.time)
    }
}
