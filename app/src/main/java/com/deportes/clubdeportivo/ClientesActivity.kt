package com.deportes.clubdeportivo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deportes.clubdeportivo.db.BDatos
import com.deportes.clubdeportivo.models.Cliente


class ClientesActivity : AppCompatActivity() {
    private lateinit var db: BDatos
    private lateinit var recyclerView: RecyclerView
    private lateinit var sinSociosText: TextView // Declaramos aquí el TextView del mensaje
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes)
        db = BDatos(this)

        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Clientes"

        btnAtras.setOnClickListener {
            finish()
        }


        // Inicializar el RecyclerView y el TextView del mensaje
        recyclerView = findViewById(R.id.recyclerSocios)
        // Es crucial que 'sinSociosText' tenga un ID en tu XML
        // y se encuentre en el mismo contenedor padre que tu RecyclerView
        sinSociosText = findViewById(R.id.textViewNoSociosMessage) // Asumiendo este ID en tu XML
        recyclerView.layoutManager = LinearLayoutManager(this) // Configurar el LayoutManager una sola vez

        var socios = db.obtenerTodosClientes()
        mostrarSociosEnPantalla(socios)

        val textBusqueda: EditText = findViewById(R.id.textBusqueda)

        textBusqueda.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || // Si usas inputType="text" y configuras imeOptions="actionSearch"
                actionId == EditorInfo.IME_ACTION_DONE ||    // Si usas inputType="text" y configuras imeOptions="actionDone"
                actionId == EditorInfo.IME_ACTION_GO ||      // Si usas inputType="text" y configuras imeOptions="actionGo"
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) // Para detectar la tecla Enter física
            ) {
                // Aquí es donde colocas la lógica que quieres ejecutar cuando el usuario presiona "Enter"
                val textoBuscado = textBusqueda.text.toString()
                if (textoBuscado.isEmpty()) {
                    socios = db.obtenerTodosClientes()
                    mostrarSociosEnPantalla(socios)
                    return@setOnEditorActionListener false
                }
                socios = db.obtenerPorDNIoId(textoBuscado)
                mostrarSociosEnPantalla(socios)
                // Ocultar el teclado virtual después de la acción
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v?.windowToken, 0)

                true // Indica que has manejado el evento
            } else {
                false // Indica que no has manejado el evento, permitiendo que otros listeners o el sistema lo manejen
            }
        }

        /* val btnRegistrarCliente = findViewById<LinearLayout>(R.id.btnNuevoCliente)



        btnRegistrarCliente.setOnClickListener {
            val registroExitosoDialog = RegistroExitosoFragment.newInstance()
            registroExitosoDialog.setOnVolverClickListener {
                // lógica al volver si querés
            }
            registroExitosoDialog.show(
                supportFragmentManager,
                RegistroExitosoFragment.TAG
            )
        }*/




    }
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