package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deportes.clubdeportivo.db.BDatos
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DescargarComprobanteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_descargar_comprobante)

        // Configurar barra superior
        val btnAtras: ImageView = findViewById(R.id.buttonBack)
        val textViewTitulo: TextView = findViewById(R.id.textViewTitle)
        textViewTitulo.text = "Comprobante de Pago"

        btnAtras.setOnClickListener {
            finish()
        }

        val idPago = intent.getIntExtra("idPago", -1)

        if (idPago == -1) {
            Toast.makeText(this, "Error: ID de pago no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val db = BDatos(this)
        val datos: Map<String, String>? = db.obtenerComprobantePorId(idPago)

        if (datos == null) {
            Toast.makeText(this, "No se encontró el comprobante", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val nombre = datos["nombre_completo"]
        val dni = datos["dni"]
        val tipoUsuario = datos["tipo_usuario"]
        val tipoPago = datos["medio_pago"]
        val fecha = datos["fecha_pago"]
        val monto = datos["monto"]
        val operacion = datos["id_pago"]

        val textoComprobante = """
            📄 COMPROBANTE DE PAGO

            Nombre: $nombre
            DNI: $dni
            Tipo: $tipoUsuario
            Pago: $tipoPago
            Fecha: $fecha
            Monto: $$monto
            Operación N°: $operacion
        """.trimIndent()

        // Mostrar datos
        findViewById<TextView>(R.id.tvNombre).text = "Nombre: $nombre"
        findViewById<TextView>(R.id.tvDNI).text = "DNI: $dni"
        findViewById<TextView>(R.id.tvTipoUsuario).text = "Tipo: $tipoUsuario"
        findViewById<TextView>(R.id.tvTipoPago).text = "Pago: $tipoPago"
        findViewById<TextView>(R.id.tvFecha).text = "Fecha: $fecha"
        findViewById<TextView>(R.id.tvMonto).text = "Monto: $$monto"
        findViewById<TextView>(R.id.tvOperacion).text = "Operación N°: $operacion"

        // Botón Compartir (FloatingActionButton con id @+id/share)
        val btnShare = findViewById<FloatingActionButton>(R.id.share)
        btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, textoComprobante)
            }
            startActivity(Intent.createChooser(shareIntent, "Compartir comprobante vía"))
        }

        // Botón Imprimir (FloatingActionButton con id @+id/print)
        val btnPrint = findViewById<FloatingActionButton>(R.id.print)
        btnPrint.setOnClickListener {
            val printIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, textoComprobante)
            }
            startActivity(Intent.createChooser(printIntent, "Imprimir comprobante vía"))
        }
    }
}
