package com.deportes.clubdeportivo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.print.PrintHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream

class VisualizarCarnet : AppCompatActivity() {

    private var mostrandoFrente = true
    private var carnetFrontResId: Int = 0
    private var carnetBackResId: Int = 0

    private lateinit var imageViewCarnet: ImageView
    private lateinit var carnetContentLayout: ConstraintLayout
    private lateinit var carnetFrameLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_carnet)

        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        btnAtras.setOnClickListener { finish() }
        textViewTitulo.text = "Carnet"

        val textViewNombreCliente = findViewById<TextView>(R.id.TextViewNombrecliente)
        val textViewApellidoCliente = findViewById<TextView>(R.id.TextViewApellidoCliente)
        val textViewIdCliente = findViewById<TextView>(R.id.TextViewIdCliente)
        val textViewDniCliente = findViewById<TextView>(R.id.TextViewDniCliente)
        val textViewAptoFisicoCliente = findViewById<TextView>(R.id.TextViewAptoFisicoCliente)
        val textViewMailCliente = findViewById<TextView>(R.id.TextViewMailCliente)
        val textViewFechaExpiracion = findViewById<TextView>(R.id.TextViewFechaExpiracion)

        imageViewCarnet = findViewById(R.id.carnetTemplateImageView)
        carnetContentLayout = findViewById(R.id.carnetContentConstraintLayout)
        carnetFrameLayout = findViewById(R.id.carnetFrameLayout)

        val idCliente = intent.getStringExtra("idCliente")
        val nombreCliente = intent.getStringExtra("nombreCliente")
        val apellidoCliente = intent.getStringExtra("apellidoCliente")
        val dniCliente = intent.getStringExtra("dniCliente")
        val email = intent.getStringExtra("email")
        val aptoFisico = intent.getIntExtra("aptoFisico", -1)
        val fechaDeExpiracion = intent.getStringExtra("fechaDeExpiracion")
        val condSocio = intent.getIntExtra("condSocio", -1)

        if (condSocio == 1) {
            carnetFrontResId = R.drawable.carnet_front_socio
            carnetBackResId = R.drawable.carnet_back_socio
            textViewFechaExpiracion.text = fechaDeExpiracion
            textViewFechaExpiracion.visibility = View.VISIBLE
        } else {
            carnetFrontResId = R.drawable.carnet_front_no_socio
            carnetBackResId = R.drawable.carnet_back_no_socio
            textViewFechaExpiracion.visibility = View.GONE
        }

        imageViewCarnet.setImageResource(carnetFrontResId)
        carnetContentLayout.visibility = View.VISIBLE

        textViewIdCliente.text = idCliente
        textViewNombreCliente.text = nombreCliente
        textViewApellidoCliente.text = apellidoCliente
        textViewDniCliente.text = dniCliente
        textViewMailCliente.text = email
        textViewAptoFisicoCliente.text = if (aptoFisico == 1) "Si" else "No"

        // --- Animación de giro del carnet, sincronizada con el contenido ---
        val btnGirarCarnet = findViewById<ImageButton>(R.id.btnGirarCarnet)
        btnGirarCarnet.setOnClickListener {
            // Ocultar contenido antes de girar si se va al reverso
            if (mostrandoFrente) {
                carnetContentLayout.visibility = View.INVISIBLE
            }

            imageViewCarnet.animate()
                .rotationXBy(90f)
                .setDuration(200)
                .withEndAction {
                    if (mostrandoFrente) {
                        imageViewCarnet.setImageResource(carnetBackResId)
                    } else {
                        imageViewCarnet.setImageResource(carnetFrontResId)
                    }

                    mostrandoFrente = !mostrandoFrente

                    imageViewCarnet.rotationX = -90f
                    imageViewCarnet.animate()
                        .rotationX(0f)
                        .setDuration(150)
                        .withEndAction {
                            if (mostrandoFrente) {
                                carnetContentLayout.visibility = View.VISIBLE
                            }
                        }
                        .start()
                }
                .start()
        }

        val shareButton = findViewById<FloatingActionButton>(R.id.share)
        shareButton.setOnClickListener {
            compartirImagen(carnetFrameLayout)
        }

        val printButton = findViewById<FloatingActionButton>(R.id.print)
        printButton.setOnClickListener {
            imprimirCarnet(carnetFrameLayout)
        }
    }

    private fun compartirImagen(view: View) {
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        val path = File(cacheDir, "images")
        path.mkdirs()
        val file = File(path, "carnet.png")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        val uri = FileProvider.getUriForFile(this, "${packageName}.provider", file)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Compartir carnet"))
    }

    private fun imprimirCarnet(view: View) {
        try {
            val bitmap = Bitmap.createBitmap(
                view.width,
                view.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            view.draw(canvas)

            val printHelper = PrintHelper(this)
            printHelper.scaleMode = PrintHelper.SCALE_MODE_FIT
            printHelper.printBitmap("Carnet del Socio", bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al imprimir el carnet", Toast.LENGTH_SHORT).show()
        }
    }
}
