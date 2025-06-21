package com.deportes.clubdeportivo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deportes.clubdeportivo.adapters.OptionAdapter
import com.deportes.clubdeportivo.db.BDatos
import com.deportes.clubdeportivo.utils.DateFormatter
import com.deportes.clubdeportivo.utils.DateFormatResult
import com.deportes.clubdeportivo.utils.ResultadoBD
import com.deportes.clubdeportivo.utils.setBottomSheetSelector
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class NuevoClienteActivity : AppCompatActivity() {
    private lateinit var db: BDatos
    private val dateFormatterUtil = DateFormatter()
    private lateinit var inputFechaNacimiento : TextView
    private lateinit var textTipoInscripcionSeleccionada: TextView
    private lateinit var textAptoFisicoSeleccionado: TextView
    private lateinit var inputNombre: TextView
    private lateinit var inputApellido: TextView
    private lateinit var inputDNI: TextView
    private lateinit var inputEmail: TextView
    private lateinit var inputTelefono: TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_cliente)

        // Mostrar el calendario
        inputFechaNacimiento = findViewById<TextView>(R.id.inputFechaNacimiento)
        val fechaNacimientoLayout = findViewById<LinearLayout>(R.id.fechaNacimientoLayout)
        fechaNacimientoLayout.setOnClickListener {
            DatePickerUtils.mostrarDatePickerDialog(this, inputFechaNacimiento)
        }
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Registrar Cliente"

        // Importación y lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        btnAtras.setOnClickListener {
            finish()
        }

        // Inicializar la base de datos
        db = BDatos(this)

        // Configuración de los campos de texto
        inputNombre = findViewById<TextView>(R.id.inputNombre)
        inputApellido = findViewById<TextView>(R.id.inputApellido)
        inputDNI = findViewById<TextView>(R.id.inputDNI)
        inputEmail = findViewById<TextView>(R.id.inputEmail)
        inputTelefono = findViewById<TextView>(R.id.inputTelefono)

        // Configuración de los spinners (BottomSheet selectors)
        val spinnerTipoDeInscripcion = findViewById<LinearLayout>(R.id.spinnerTipoDeInscripcion)
        val spinnerAptoFisico = findViewById<LinearLayout>(R.id.spinnerAptoFisico)
        textTipoInscripcionSeleccionada = findViewById<TextView>(R.id.inputTipoInscripcion)
        textAptoFisicoSeleccionado = findViewById<TextView>(R.id.inputAptoFisico)

        // Opciones para los spinners
        val opcionesTipoDeInscripcion = listOf("Socio", "No Socio")
        val opcionesAptoFisico = listOf("Posee", "No posee")

        // Configurar los listeners para los spinners
        setBottomSheetSelector(spinnerTipoDeInscripcion, opcionesTipoDeInscripcion, "Tipo de Inscripción", textTipoInscripcionSeleccionada)
        setBottomSheetSelector(spinnerAptoFisico, opcionesAptoFisico, "Apto Físico", textAptoFisicoSeleccionado)




        val btnRegistrarCliente: Button = findViewById<Button>(R.id.btnRegistrarCliente)



        btnRegistrarCliente.setOnClickListener {
            when (val resultado = guardarNuevoCliente()) {
                is ResultadoBD.Exito -> {
                    val registroExitosoDialog =
                        RegistroExitosoFragment.newInstance() // Usar el nuevo nombre de la clase
                    registroExitosoDialog.setOnVolverClickListener {
                        val cliente = db.ejecutarConsultaSelect("SELECT * FROM Cliente WHERE id_cliente = ?", arrayOf(resultado.datos.toString()))
                        var intent : Intent
                        val idCliente = resultado.datos.toString()
                        //verificar que el cliente sea socio
                        if (cliente[0]["cond_socio"] == 1){
                            intent = Intent(this, PagosDetallesSocioActivity::class.java)
                            intent.putExtra("nombre", cliente[0]["nombre"].toString())
                            intent.putExtra("apellido", cliente[0]["apellido"].toString())
                            intent.putExtra("dni", cliente[0]["dni"].toString())
                            intent.putExtra("id_cliente", idCliente.toInt())
                            startActivity(intent)
                        } else {
                            intent = Intent(this, PagosDetallesNoSocioActivity::class.java)
                            intent.putExtra("nombre", cliente[0]["nombre"].toString())
                            intent.putExtra("apellido", cliente[0]["apellido"].toString())
                            intent.putExtra("dni", cliente[0]["dni"].toString())
                            intent.putExtra("id_cliente", idCliente.toInt())
                            startActivity(intent)
                        }


                    }
                    registroExitosoDialog.show(
                        supportFragmentManager,
                        CambioExitosoFragment.TAG
                    ) // Usar el nuevo TAG
                }
                ResultadoBD.YaExiste -> {
                    Toast.makeText(this, "El cliente ya existe", Toast.LENGTH_SHORT).show()
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

    private fun guardarNuevoCliente(): ResultadoBD<Int> {
        val nombre = inputNombre.text.toString().trim()
        val apellido = inputApellido.text.toString().trim()
        val dni = inputDNI.text.toString().trim()
        val email = inputEmail.text.toString().trim()
        val telefono = inputTelefono.text.toString().trim()
        val fechaNacimientoTexto = inputFechaNacimiento.text.toString().trim()
        val tipoInscripcion = textTipoInscripcionSeleccionada.text.toString().trim()
        val aptoFisico = textAptoFisicoSeleccionado.text.toString().trim()

        // Validación de campos
        // --- 1. Validación de Campos Vacíos ---
        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || email.isEmpty()
            || telefono.isEmpty() || fechaNacimientoTexto.isEmpty()
            || tipoInscripcion.isEmpty() || aptoFisico.isEmpty()
        ) {
            return ResultadoBD.CamposIncompletos
        }

        // --- 2. Validaciones de Formato y Tamaño Mínimo ---
        // Nombre y Apellido
        if (nombre.length < 2) {
            return ResultadoBD.Error("El nombre debe tener al menos 2 caracteres.")
        }
        if (apellido.length < 2) {
            return ResultadoBD.Error("El apellido debe tener al menos 2 caracteres.")
        }

        // DNI (Ejemplo: 7 u 8 dígitos numéricos)
        if (!dni.matches(Regex("^\\d{7,9}$"))) {
            return ResultadoBD.Error("El DNI debe contener 7 u 8 dígitos numéricos.")
        }

        // Email (Validación básica de formato de email)
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ResultadoBD.Error("El formato del email no es válido.")
        }

        // Teléfono (Ejemplo: Mínimo 7 dígitos, solo números, puedes ajustar la expresión regular)
        if (!telefono.matches(Regex("^\\d{7,}$"))) {
            return ResultadoBD.Error("El número de teléfono debe tener al menos 7 dígitos y solo números.")
        }

        // El cliente debe poseer apto fisico
        if (aptoFisico != "Posee") {
            return ResultadoBD.Error("El cliente debe poseer apto físico.")
        }

        val condSocio = tipoInscripcion == "Socio"
        val aptoFisicoBool = aptoFisico == "Posee"

        val fechaNacimientoFormateada: String
        when (val result = dateFormatterUtil.formatInputDateForDatabase(fechaNacimientoTexto)) {
            is DateFormatResult.Success -> {
                fechaNacimientoFormateada = result.formattedDate
            }
            is DateFormatResult.Error -> {
                // Si hay un error de formato de fecha, retornamos un ResultadoBD.Error
                return ResultadoBD.Error(result.errorMessage)
            }
        }

        return try {
            val queryCliente = "SELECT id_cliente FROM Cliente WHERE dni = ?"
            val argsCliente = arrayOf(dni)
            val resultadoCliente = db.ejecutarConsultaSelect(queryCliente, argsCliente)

            if (resultadoCliente.isNotEmpty()) {
                ResultadoBD.YaExiste
            } else {
                val insertClienteQuery = """
                INSERT INTO Cliente (nombre, apellido, dni, email, telefono, fecha_nacimiento, cond_socio, apto_fisico)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """.trimIndent()

                val insertClienteArgs = arrayOf(
                    nombre,
                    apellido,
                    dni,
                    email,
                    telefono,
                    fechaNacimientoFormateada,
                    if (condSocio) "1" else "0",
                    if (aptoFisicoBool) "1" else "0"
                )


                val idCliente = db.insertar(insertClienteQuery, insertClienteArgs)

                if (idCliente != null && idCliente > 0) {
                    ResultadoBD.Exito(idCliente)
                } else {
                    ResultadoBD.Error("Error al insertar el cliente en la base de datos")
                }
            }
        } catch (e: Exception) {
            ResultadoBD.Error("Excepción: ${e.message}")
        }
    }






}
