package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deportes.clubdeportivo.db.BDatos
import com.deportes.clubdeportivo.utils.DateFormatResult
import com.deportes.clubdeportivo.utils.DateFormatter
import com.deportes.clubdeportivo.utils.ResultadoBD

class ActualizacionDatosActivity : AppCompatActivity() {
    private val dateFormatterUtil = DateFormatter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizacion_datos)

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Clientes"

        btnAtras.setOnClickListener {
            finish()
        }
        val idSocio = intent.getIntExtra("id_socio", -1)
        // obtengo el socio desde la base de datos
        val db = BDatos(this)
        val socio = db.obtenerSocioPorId(idSocio)
        if (socio == null) {
            // Manejar el caso en que el socio no se encuentra en la base de datos
            // mensaje de error y despues volver a la pantalla anterior
            Toast.makeText(this, "Socio no encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        // Lógica de muestreo de calendario
        val  inputFechaNacimiento = findViewById<TextView>(R.id.inputFechaNacimiento)
        val fechaNacimientoLayout = findViewById<LinearLayout>(R.id.fechaNacimientoLayout)
        // formateo la fecha de nacimientoinputFechaNacimiento
        val fechaNacimientoFormateada: String
        when (val result = dateFormatterUtil.formatDateForDisplay(socio!!.fechaNacimiento ?: "")) {
            is DateFormatResult.Success -> {
                fechaNacimientoFormateada = result.formattedDate
            }
            is DateFormatResult.Error -> {
                // Si hay un error de formato de fecha, mostramos la fecha de hoy
                fechaNacimientoFormateada = dateFormatterUtil.getCurrentDate()
                Toast.makeText(this, "Error al formatear la fecha de nacimiento", Toast.LENGTH_SHORT).show()

            }
        }

        inputFechaNacimiento.text = fechaNacimientoFormateada


        fechaNacimientoLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaNacimiento) // Pasar el contexto de la actividad
        }

        val inputNombre = findViewById<TextView>(R.id.editTextNombre)
        inputNombre.text = socio.nombre
        val inputApellido = findViewById<TextView>(R.id.editTextApellido)
        inputApellido.text = socio.apellido
        val inputDni = findViewById<TextView>(R.id.editTextDni)
        inputDni.text = socio.dni
        val inputMail = findViewById<TextView>(R.id.editTextMail)
        inputMail.text = socio.email
        var tipoInscripcion = "Socio"
        if (socio.condSocio == false) {
            tipoInscripcion = "No Socio"
        }
        var aptoFisico = "Posee"
        if (socio.aptoFisico == false) {
            aptoFisico = "No posee"
        }
        val textViewTipoInscripcion = findViewById<TextView>(R.id.textViewTipoInscripcion)
        textViewTipoInscripcion.text = tipoInscripcion
        val textViewAptoFisico = findViewById<TextView>(R.id.textViewAptoFisico)
        textViewAptoFisico.text = aptoFisico
        //val inputTelefono = findViewById<TextView>(R.id.editTextTelefono)
        //inputTelefono.text = socio.telefono





        // Lógica del boton actualizar cambios
        val btnActualizarCambios = findViewById<Button>(R.id.buttonActualizarDatos)

        btnActualizarCambios.setOnClickListener {
            val cambioExitosoDialog =
                CambioExitosoFragment.newInstance() // Usar el nuevo nombre de la clase
            cambioExitosoDialog.setOnVolverClickListener {
                // ... lógica al volver ...
            }
            cambioExitosoDialog.show(
                supportFragmentManager,
                CambioExitosoFragment.TAG
            ) // Usar el nuevo TAG
        }
    }



}
