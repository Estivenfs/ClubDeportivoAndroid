package com.deportes.clubdeportivo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.print.PrintHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream


class VisualizarCarnet : AppCompatActivity() {

    private var mostrandoFrente = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_carnet)

        // Lógica de la barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)

        textViewTitulo.text = "Carnet"

        btnAtras.setOnClickListener {
            finish()
        }


        val carnet = findViewById<ImageView>(R.id.carnet)
        val girarCarnet = findViewById<ImageButton>(R.id.girarcarnet)

        girarCarnet.setOnClickListener {
            carnet.animate()
                .rotationY(90f)
                .setDuration(150)
                .withEndAction {
                    carnet.setImageResource(
                        if (mostrandoFrente) R.drawable.carnet_back else R.drawable.carnet_front
                    )
                    mostrandoFrente = !mostrandoFrente
                    carnet.rotationY = -90f
                    carnet.animate().rotationY(0f).setDuration(150).start()
                }
                .start()
        }


        val shareButton = findViewById<FloatingActionButton>(R.id.share)
        shareButton.setOnClickListener {
            compartirImagen(carnet)
        }


        val printButton = findViewById<FloatingActionButton>(R.id.print)
        printButton.setOnClickListener {
            imprimirCarnet(carnet)
        }
    }


    private fun compartirImagen(imageView: ImageView) {
        val bitmap = Bitmap.createBitmap(
            imageView.width,
            imageView.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        imageView.draw(canvas)

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


    private fun imprimirCarnet(imageView: ImageView) {
        try {
            val bitmap = Bitmap.createBitmap(
                imageView.width,
                imageView.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            imageView.draw(canvas)

            val printHelper = PrintHelper(this)
            printHelper.scaleMode = PrintHelper.SCALE_MODE_FIT
            printHelper.printBitmap("Carnet del Socio", bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al imprimir el carnet", Toast.LENGTH_SHORT).show()
        }
    }
}
