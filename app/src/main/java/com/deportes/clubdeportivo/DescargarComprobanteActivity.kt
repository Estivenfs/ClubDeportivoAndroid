package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deportes.clubdeportivo.db.BDatos

class DescargarComprobanteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_descargar_comprobante)

        // ⚠️ Declarar una sola vez
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

        // ✅ Usar safe call ?.get() o !! si estás seguro
        findViewById<TextView>(R.id.tvNombre).text = "Nombre: ${datos["nombre_completo"]}"
        findViewById<TextView>(R.id.tvDNI).text = "DNI: ${datos["dni"]}"
        findViewById<TextView>(R.id.tvTipoUsuario).text = "Tipo: ${datos["tipo_usuario"]}"
        findViewById<TextView>(R.id.tvTipoPago).text = "Pago: ${datos["medio_pago"]}"
        findViewById<TextView>(R.id.tvFecha).text = "Fecha: ${datos["fecha_pago"]}"
        findViewById<TextView>(R.id.tvMonto).text = "Monto: $${datos["monto"]}"
        findViewById<TextView>(R.id.tvOperacion).text = "Operación N°: ${datos["id_pago"]}"

        val btnCompartir = findViewById<Button>(R.id.btnCompartir)
        btnCompartir.setOnClickListener {
            val texto = """
                📄 COMPROBANTE DE PAGO

                Nombre: ${datos["nombre_completo"]}
                DNI: ${datos["dni"]}
                Tipo: ${datos["tipo_usuario"]}
                Pago: ${datos["medio_pago"]}
                Fecha: ${datos["fecha_pago"]}
                Monto: $${datos["monto"]}
                Operación N°: ${datos["id_pago"]}
            """.trimIndent()

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, texto)
            }

            startActivity(Intent.createChooser(shareIntent, "Compartir comprobante vía"))
        }
    }
}
