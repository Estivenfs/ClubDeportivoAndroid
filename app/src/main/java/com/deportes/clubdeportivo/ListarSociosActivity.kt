package com.deportes.clubdeportivo

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deportes.clubdeportivo.db.BDatos
import android.graphics.Color
import android.view.Gravity


class ListarSociosActivity : AppCompatActivity() {
    private lateinit var db: BDatos
    private lateinit var recyclerView: RecyclerView
    private lateinit var sinSociosText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_socios)

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

        val socios = db.obtenerSocios() // implementa esta función en BDatos

        if (socios.isEmpty()) {
            (recyclerView.parent as ViewGroup).addView(sinSociosText)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = SocioAdapter(socios)
        }
    }
}
