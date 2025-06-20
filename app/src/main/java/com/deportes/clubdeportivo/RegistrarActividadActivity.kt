package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deportes.clubdeportivo.db.BDatos
import com.deportes.clubdeportivo.utils.ResultadoBD
import com.deportes.clubdeportivo.utils.setBottomSheetSelector

class RegistroActividadActivity : AppCompatActivity() {
    private lateinit var db: BDatos
    private lateinit var textActividadSeleccionada: TextView
    private lateinit var inputNombreActividad: EditText
    private lateinit var inputPrecioCuota: EditText
    private lateinit var inputCupoActividad: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_actividad)

        db = BDatos(this)

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Registro de Actividad"

        btnAtras.setOnClickListener {
            finish()
        }

        val btnRegistrarActividad = findViewById<Button>(R.id.btnRegistrarActividad)



        // Configuración de los campos de texto
        inputNombreActividad = findViewById<EditText>(R.id.inputNombreActividad)
        inputPrecioCuota = findViewById<EditText>(R.id.inputPrecioCuota)
        inputCupoActividad = findViewById<EditText>(R.id.inputCupoActividad)


        // Lógica al registrar actividad
        btnRegistrarActividad.setOnClickListener {
            when (val resultado = guardarNuevaActividad()) {
                is ResultadoBD.Exito -> {
                    //cargarActividadesEnSpinner()
                    val registroExitosoDialog = RegistroExitosoFragment.newInstance()
                    registroExitosoDialog.setOnVolverClickListener {
                        // ... lógica al volver ...
                    }
                    registroExitosoDialog.show(
                        supportFragmentManager, RegistroExitosoFragment.TAG
                    ) // Usar el nuevo TAG
                    // Vaciar los campos después de guardar
                    inputNombreActividad.text.clear()
                    inputPrecioCuota.text.clear()
                    inputCupoActividad.text.clear()
                }
                ResultadoBD.YaExiste -> {
                    Toast.makeText(this, "La actividad ya existe", Toast.LENGTH_SHORT).show()
                }
                is ResultadoBD.Error -> {
                    Toast.makeText(this, "Error: ${resultado.mensaje}", Toast.LENGTH_LONG).show()
                }
                ResultadoBD.CamposIncompletos -> {
                    Toast.makeText(this, "Por favor completa todos los campos.", Toast.LENGTH_SHORT).show()
                }

            }

        }

    }

    /*private fun cargarActividadesEnSpinner() {
        val opcionesActividad = db.obtenerActividades()
        val textActividadSeleccionada = findViewById<TextView>(R.id.inputActividadSeleccionada)
        val spinnerActividad = findViewById<LinearLayout>(R.id.spinnerActividad)

        if (opcionesActividad.isEmpty()) {
            textActividadSeleccionada.text = "No hay actividades disponibles"
            spinnerActividad.setOnClickListener(null) // Desactivar si no hay opciones
        } else {
            textActividadSeleccionada.text = opcionesActividad[0]
            setBottomSheetSelector(spinnerActividad, opcionesActividad, "Actividad", textActividadSeleccionada)
        }
    }*/


    fun guardarNuevaActividad() : ResultadoBD<Int>{
        val nombreActividad = inputNombreActividad.text.toString().trim()
        val precioCuotaText = inputPrecioCuota.text.toString().trim()
        val cupoActividadText = inputCupoActividad.text.toString().trim()
        var precioCuota : Float
        if (nombreActividad.isEmpty() || precioCuotaText.isEmpty() || cupoActividadText.isEmpty()) {
            return ResultadoBD.CamposIncompletos
        }
        try {
            precioCuota = precioCuotaText.toFloat()
        } catch (e: NumberFormatException) {
            return ResultadoBD.Error("El precio por cuota debe ser un número válido")
        }

        val cupoActividad = cupoActividadText.toIntOrNull()
        if (cupoActividad == null) {
            return ResultadoBD.Error("El cupo de la actividad debe ser un número válido")
        }


        val insertActividadQuery = """
            INSERT INTO Actividades (nombre_actividad, precio_actividad, cupo_actividad)
            VALUES (?, ?, ?)
        """.trimIndent()
        val selectActividadQuery = """
            SELECT id_actividad FROM Actividades WHERE nombre_actividad = ?
        """.trimIndent()
        return try {
            val resultadoActividad = db.ejecutarConsultaSelect(selectActividadQuery, arrayOf(nombreActividad))
            if (resultadoActividad.isNotEmpty()) {
                ResultadoBD.YaExiste
            } else {
                val insertActividadArgs = arrayOf(nombreActividad, precioCuota.toString(), cupoActividad.toString())
                val idActividad = db.insertar(insertActividadQuery, insertActividadArgs)
                if (idActividad != null && idActividad > 0) {
                    ResultadoBD.Exito(idActividad)
                } else {
                    ResultadoBD.Error("Error al insertar la actividad en la base de datos")
                }
            }
        } catch (e: Exception) {
            ResultadoBD.Error("Excepción: ${e.message}")

        }


    }

    /*fun actualizarListadoActividades(fn: () -> Unit){
        val opcionesActividad = db.obtenerActividades()
        if (opcionesActividad.isEmpty()) {
            textActividadSeleccionada.text = "No hay actividades disponibles"
        } else {
            textActividadSeleccionada.text = opcionesActividad[0]
            fn()
        }
    }*/


}
