package com.deportes.clubdeportivo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment


class CargandoFragment : DialogFragment() {

    companion object {
        const val TAG = "CargandoFragment"  // Para identificarlo en el FragmentManager
        fun newInstance(): CargandoFragment {
            return CargandoFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del Fragment.  Crea un layout llamado fragment_loading.xml
        return inflater.inflate(R.layout.fragment_cargando, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Configurar el estilo del diálogo para que sea "Full Screen"
        setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)

        // Aquí puedes configurar la apariencia de tu pantalla de carga:
        //  - Un ProgressBar
        //  - Un TextView con un mensaje ("Cargando...", "Procesando...")
        //  - Una animación
    }

    override fun onStart() {
        super.onStart()
        // Hacer que el diálogo no se pueda cancelar tocando afuera.
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCancelable(false)
    }
}