package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

class BarraInferiorFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_barra_inferior, container, false)

        val btnMenu = view.findViewById<LinearLayout>(R.id.btnMenu)
        val btnNuevoCliente = view.findViewById<LinearLayout>(R.id.btnNuevoCliente)
        val btnConsultas = view.findViewById<LinearLayout>(R.id.btnConsultas)
        val btnPagos = view.findViewById<LinearLayout>(R.id.btnPagos)

        btnMenu.setOnClickListener {
            startActivity(Intent(requireContext(), MenuPrincipalActivity::class.java))
        }

        btnNuevoCliente.setOnClickListener {
            startActivity(Intent(requireContext(), NuevoClienteActivity::class.java))
        }

        btnConsultas.setOnClickListener {
            startActivity(Intent(requireContext(), ConsultasActivity::class.java))
        }

        btnPagos.setOnClickListener {
            startActivity(Intent(requireContext(), PagosBusquedaActivity::class.java))
        }

        return view
    }
}
