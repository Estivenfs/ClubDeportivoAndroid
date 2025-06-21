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
import android.view.View // Importar View para controlar la visibilidad
import com.deportes.clubdeportivo.models.Cliente
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.min


class ListarSociosActivity : AppCompatActivity() {
    private lateinit var db: BDatos
    private lateinit var recyclerView: RecyclerView
    private lateinit var sinSociosText: TextView // Declaramos aquí el TextView del mensaje

    // Botones para controlar el estado visual y la lógica
    private lateinit var btnTodos: Button
    private lateinit var btnSinActividad: Button
    private lateinit var btnProximos: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_socios)

        db = BDatos(this)

        // Inicializar el RecyclerView y el TextView del mensaje
        recyclerView = findViewById(R.id.recyclerSocios)
        // Es crucial que 'sinSociosText' tenga un ID en tu XML
        // y se encuentre en el mismo contenedor padre que tu RecyclerView
        sinSociosText = findViewById(R.id.textViewNoSociosMessage) // Asumiendo este ID en tu XML
        recyclerView.layoutManager = LinearLayoutManager(this) // Configurar el LayoutManager una sola vez

        // Inicializar vistas de los botones
        btnTodos = findViewById(R.id.btnTodos)
        btnSinActividad = findViewById(R.id.btnSinActividad)
        btnProximos = findViewById(R.id.btnProximos)

        // Configurar listeners de los botones de filtro
        btnTodos.setOnClickListener {
            val socios = db.obtenerTodosSocios()
            mostrarSociosEnPantalla(socios)
            // Actualizar la apariencia de los botones
            btnTodos.alpha = 1.0f
            btnSinActividad.alpha = 0.5f
            btnProximos.alpha = 0.5f
        }

        btnSinActividad.setOnClickListener {
            val socios = db.obtenerVencidos()
            mostrarSociosEnPantalla(socios)
            // Actualizar la apariencia de los botones
            btnTodos.alpha = 0.5f
            btnSinActividad.alpha = 1.0f
            btnProximos.alpha = 0.5f
        }

        btnProximos.setOnClickListener {
            val socios = db.obtenerClientesConPagoMesAnterior()
            mostrarSociosEnPantalla(socios)
            // Actualizar la apariencia de los botones
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

        // Cargar la lista inicial de socios (por ejemplo, "Próximos")
        btnProximos.performClick() // Simula un clic en el botón "Próximos" para cargar inicialmente
    }

    // --- Función para mostrar/ocultar el RecyclerView y el mensaje ---
    fun mostrarSociosEnPantalla(socios: List<Cliente>) {
        if (socios.isEmpty()) {
            // Si no hay socios, oculta el RecyclerView y muestra el mensaje
            recyclerView.visibility = View.GONE
            sinSociosText.visibility = View.VISIBLE
        } else {
            // Si hay socios, muestra el RecyclerView y oculta el mensaje
            recyclerView.visibility = View.VISIBLE
            sinSociosText.visibility = View.GONE

            // Asigna un nuevo adaptador o actualiza el existente
            // Si SocioAdapter tuviera un método para actualizar la lista (ej. updateData(newList)),
            // sería más eficiente que crear uno nuevo cada vez.
            // Por ejemplo: (recyclerView.adapter as? SocioAdapter)?.updateData(socios) ?: run { ... }
            recyclerView.adapter = SocioAdapter(socios)
            // Notificar al adaptador de los cambios para asegurar que la vista se actualice
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }
}