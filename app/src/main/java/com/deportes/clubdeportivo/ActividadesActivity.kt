package com.deportes.clubdeportivo

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deportes.clubdeportivo.db.BDatos
import com.deportes.clubdeportivo.models.Actividad
import com.deportes.clubdeportivo.ActividadesAdapter


class ActividadesActivity : AppCompatActivity() {
    private lateinit var db: BDatos
    private lateinit var recyclerView: RecyclerView
    private lateinit var sinActividadesText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividades)


        db = BDatos(this) //

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Actividades"

        btnAtras.setOnClickListener {
            finish()
        }

        // Lógica para mostrar la lista de actividades
        recyclerView = findViewById(R.id.recyclerActividades)
        sinActividadesText = TextView(this).apply {
            text = "No hay actividades registradas"
            setTextColor(Color.WHITE)
            textSize = 18f
            gravity = Gravity.CENTER
        }

        val actividades = db.obtenerTodasLasActividades()

        if (actividades.isEmpty()) {
            (recyclerView.parent as ViewGroup).addView(sinActividadesText)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = ActividadesAdapter(actividades)
        }
    }
}