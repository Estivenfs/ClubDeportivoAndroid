package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText // Importar EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deportes.clubdeportivo.db.BDatos
import com.deportes.clubdeportivo.utils.DateFormatResult
import com.deportes.clubdeportivo.utils.DateFormatter
import com.deportes.clubdeportivo.utils.ResultadoBD
import com.deportes.clubdeportivo.utils.setBottomSheetSelector // Importar si no está

class ActualizacionDatosActivity : AppCompatActivity() {
    private val dateFormatterUtil = DateFormatter()
    private lateinit var db: BDatos // Inicializar db
    private lateinit var inputFechaNacimiento: TextView // Sigue siendo TextView para mostrar y usar el DatePicker
    private lateinit var textTipoInscripcionSeleccionada: TextView
    private lateinit var textAptoFisicoSeleccionado: TextView

    // EditTexts para los campos editables
    private lateinit var inputNombre: EditText
    private lateinit var inputApellido: EditText
    private lateinit var inputDni: EditText // Este será no editable
    private lateinit var inputEmail: EditText
    private lateinit var inputTelefono: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizacion_datos)

        // Inicializar la base de datos
        db = BDatos(this)

        // --- Lógica de la barra superior ---
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Actualizar Cliente" // Cambiamos el título

        btnAtras.setOnClickListener {
            finish()
        }

        // --- Obtener ID del socio y cargar datos ---
        val idSocio = intent.getIntExtra("id_socio", -1) // Usar la clave "ID_SOCIO"
        if (idSocio == -1) {
            Toast.makeText(this, "Error: ID de socio no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val socio = db.obtenerSocioPorId(idSocio)
        if (socio == null) {
            Toast.makeText(this, "Socio no encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // --- Inicializar vistas y pre-llenar con datos del socio ---
        // Campo de fecha de nacimiento (TextView con DatePicker)
        inputFechaNacimiento = findViewById(R.id.inputFechaNacimiento)
        val fechaNacimientoLayout = findViewById<LinearLayout>(R.id.fechaNacimientoLayout)
        val fechaNacimientoFormateada: String
        when (val result = dateFormatterUtil.formatDateForDisplay(socio.fechaNacimiento ?: "")) {
            is DateFormatResult.Success -> {
                fechaNacimientoFormateada = result.formattedDate
            }
            is DateFormatResult.Error -> {
                fechaNacimientoFormateada = dateFormatterUtil.getCurrentDate()
                Toast.makeText(this, "Error al formatear la fecha de nacimiento", Toast.LENGTH_SHORT).show()
            }
        }
        inputFechaNacimiento.text = fechaNacimientoFormateada

        fechaNacimientoLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaNacimiento)
        }

        // EditTexts
        inputNombre = findViewById(R.id.editTextNombre)
        inputNombre.setText(socio.nombre)

        inputApellido = findViewById(R.id.editTextApellido)
        inputApellido.setText(socio.apellido)

        inputDni = findViewById(R.id.editTextDni)
        inputDni.setText(socio.dni)
        // HACER EL DNI NO EDITABLE
        inputDni.isEnabled = false
        inputDni.isFocusable = false
        inputDni.isLongClickable = false
        inputDni.isCursorVisible = false
        // Opcional: para que se vea visualmente deshabilitado si el drawable no lo hace
        // inputDni.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.darker_gray)


        inputEmail = findViewById(R.id.editTextMail)
        inputEmail.setText(socio.email)

        inputTelefono = findViewById(R.id.editTextTelefono)
        inputTelefono.setText(socio.telefono)

        // Spinners (BottomSheet selectors)
        val spinnerTipoDeInscripcion = findViewById<LinearLayout>(R.id.desplegableTipoInscripcion) // Asumo que tienes un LinearLayout para el spinner
        val spinnerAptoFisico = findViewById<LinearLayout>(R.id.desplegableAptoFisico) // Asumo que tienes un LinearLayout para el spinner
        textTipoInscripcionSeleccionada = findViewById(R.id.textViewTipoInscripcion)
        textAptoFisicoSeleccionado = findViewById(R.id.textViewAptoFisico)

        val opcionesTipoDeInscripcion = listOf("Socio", "No Socio")
        val opcionesAptoFisico = listOf("Posee", "No posee")

        // Establecer el valor inicial en los TextViews de los spinners
        textTipoInscripcionSeleccionada.text = if (socio.condSocio == true) "Socio" else "No Socio"
        textAptoFisicoSeleccionado.text = if (socio.aptoFisico == true) "Posee" else "No posee"

        // Configurar los listeners para los spinners
        setBottomSheetSelector(spinnerTipoDeInscripcion, opcionesTipoDeInscripcion, "Tipo de Inscripción", textTipoInscripcionSeleccionada)
        setBottomSheetSelector(spinnerAptoFisico, opcionesAptoFisico, "Apto Físico", textAptoFisicoSeleccionado)

        // --- Lógica del botón actualizar cambios ---
        val btnActualizarCambios = findViewById<Button>(R.id.buttonActualizarDatos)

        btnActualizarCambios.setOnClickListener {
            when (val resultado = actualizarCliente(idSocio)) {
                is ResultadoBD.Exito -> {
                    val cambioExitosoDialog = CambioExitosoFragment.newInstance()
                    cambioExitosoDialog.setOnVolverClickListener {
                        finish() // Volver a la pantalla anterior
                    }
                    cambioExitosoDialog.show(supportFragmentManager, CambioExitosoFragment.TAG)
                }
                ResultadoBD.YaExiste -> {
                    // Aunque el DNI no es editable, podría haber otros campos que generen "ya existe"
                    // En este contexto de actualización, "ya existe" no debería ocurrir por DNI.
                    // Podrías mostrar un mensaje más específico si lo consideras.
                    Toast.makeText(this, "No se realizaron cambios o el DNI ya existe", Toast.LENGTH_SHORT).show()
                }
                is ResultadoBD.Error -> {
                    Toast.makeText(this, "Error al actualizar: ${resultado.mensaje}", Toast.LENGTH_LONG).show()
                }
                ResultadoBD.CamposIncompletos -> {
                    Toast.makeText(this, "Por favor completa todos los campos.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // --- Función para actualizar el cliente en la base de datos ---
    private fun actualizarCliente(idSocio: Int): ResultadoBD<Int> {
        val nombre = inputNombre.text.toString().trim()
        val apellido = inputApellido.text.toString().trim()
        val dni = inputDni.text.toString().trim() // No editable, pero lo obtenemos
        val email = inputEmail.text.toString().trim()
        val telefono = inputTelefono.text.toString().trim()
        val fechaNacimientoTexto = inputFechaNacimiento.text.toString().trim()
        val tipoInscripcion = textTipoInscripcionSeleccionada.text.toString().trim()
        val aptoFisico = textAptoFisicoSeleccionado.text.toString().trim()

        // Validación de campos
        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || email.isEmpty()
            || telefono.isEmpty() || fechaNacimientoTexto.isEmpty()
            || tipoInscripcion.isEmpty() || aptoFisico.isEmpty()
        ) {
            return ResultadoBD.CamposIncompletos
        }

        val condSocio = tipoInscripcion == "Socio"
        val aptoFisicoBool = aptoFisico == "Posee"

        val fechaNacimientoFormateada: String
        when (val result = dateFormatterUtil.formatInputDateForDatabase(fechaNacimientoTexto)) {
            is DateFormatResult.Success -> {
                fechaNacimientoFormateada = result.formattedDate
            }
            is DateFormatResult.Error -> {
                return ResultadoBD.Error(result.errorMessage)
            }
        }

        return try {
            val updateClienteQuery = """
                UPDATE Cliente
                SET nombre = ?, apellido = ?, email = ?, telefono = ?, fecha_nacimiento = ?, cond_socio = ?, apto_fisico = ?
                WHERE id_cliente = ?
            """.trimIndent()

            val updateClienteArgs = arrayOf(
                nombre,
                apellido,
                email,
                telefono,
                fechaNacimientoFormateada,
                if (condSocio) "1" else "0",
                if (aptoFisicoBool) "1" else "0",
                idSocio.toString() // El ID del cliente para la cláusula WHERE
            )

            // Asumiendo que BDatos.actualizar() devuelve un Boolean o un Int de filas afectadas
            val filasAfectadas = db.actualizarOEliminar(updateClienteQuery, updateClienteArgs)

            if (filasAfectadas > 0) {
                ResultadoBD.Exito(idSocio) // Devolver el ID del cliente actualizado
            } else {
                ResultadoBD.Error("No se pudo actualizar el cliente. Posiblemente no se realizaron cambios o el ID no existe.")
            }
        } catch (e: Exception) {
            ResultadoBD.Error("Excepción al actualizar: ${e.message}")
        }
    }
}