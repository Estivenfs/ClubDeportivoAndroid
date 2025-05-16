package com.deportes.clubdeportivo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.core.graphics.toColorInt

class BarraInferiorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_barra_inferior, container, false)

        // Referencias a botones
        val btnMenu = view.findViewById<LinearLayout>(R.id.btnMenu)
        val btnNuevoCliente = view.findViewById<LinearLayout>(R.id.btnNuevoCliente)
        val btnConsultas = view.findViewById<LinearLayout>(R.id.btnConsultas)
        val btnPagos = view.findViewById<LinearLayout>(R.id.btnPagos)

        // Asignar comportamiento según actividad actual
        setupNavigationItem(btnMenu, MenuPrincipalActivity::class.java)
        setupNavigationItem(btnNuevoCliente, NuevoClienteActivity::class.java)
        setupNavigationItem(btnConsultas, ConsultasActivity::class.java)
        setupNavigationItem(btnPagos, PagosBusquedaActivity::class.java)

        return view
    }

    private fun setupNavigationItem(button: LinearLayout, targetActivity: Class<*>) {
        val currentActivity = requireActivity()::class.java

        // Obtener el icono y texto del botón
        val imageView = button.getChildAt(0) as ImageView
        val textView = button.getChildAt(1) as TextView

        if (currentActivity == targetActivity) {
            // Cambiar color para indicar que esta es la Activity actual
            imageView.setColorFilter(Color.WHITE) // o el color que quieras para activo
            textView.setTextColor(Color.WHITE)

            // Desactivar el click
            button.isClickable = false
        } else {
            // Colores por defecto
            imageView.setColorFilter("#D0BA12".toColorInt())
            textView.setTextColor("#D0BA12".toColorInt())

            // Redirigir a la actividad correspondiente
            button.setOnClickListener {
                startActivity(Intent(requireContext(), targetActivity))
            }
        }
    }
}
