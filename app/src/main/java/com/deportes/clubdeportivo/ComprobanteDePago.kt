package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ComprobanteDePago : AppCompatActivity() {

    // Simulamos el ID del pago que debería venir de la base de datos después de hacer el pago
    private var idPagoGenerado: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprobante_de_pago)

        // Recuperar el ID de pago enviado desde la pantalla anterior
        idPagoGenerado = intent.getIntExtra("id_pago", -1)

        // Configurar barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Comprobante de Pago"

        btnAtras.setOnClickListener {
            finish()
        }

        // Botón DESCARGAR COMPROBANTE
        val btnImprimir = findViewById<Button>(R.id.btnImprimir)
        btnImprimir.setOnClickListener {
            if (idPagoGenerado != -1) {
                val intent = Intent(this, DescargarComprobanteActivity::class.java)
                intent.putExtra("id_pago", idPagoGenerado)
                startActivity(intent)
            } else {
                // Manejo de error si no se pasó un id válido
                android.widget.Toast.makeText(this, "Error: Pago no registrado", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
}
