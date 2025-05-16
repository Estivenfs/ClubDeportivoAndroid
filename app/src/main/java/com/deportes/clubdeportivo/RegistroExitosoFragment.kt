package com.deportes.clubdeportivo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

class RegistroExitosoFragment : DialogFragment() {

    private var onVolverClickListener: (() -> Unit)? = null
    private var volverButton: Button? = null
    private var mostrarVolver = true // Nuevo: controla la visibilidad del botón

    companion object {
        const val TAG = "RegistroExitosoFragment"
        private const val ARG_MOSTRAR_VOLVER = "mostrarVolver"

        fun newInstance(mostrarVolver: Boolean = true): RegistroExitosoFragment { // Valor por defecto = true
            val fragment = RegistroExitosoFragment()
            val args = Bundle()
            args.putBoolean(ARG_MOSTRAR_VOLVER, mostrarVolver)
            fragment.arguments = args
            return fragment
        }
    }

    fun setOnVolverClickListener(listener: () -> Unit) {
        onVolverClickListener = listener
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtener el argumento al crear el fragment
        mostrarVolver = arguments?.getBoolean(ARG_MOSTRAR_VOLVER, true) ?: true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del Fragment.
        val view = inflater.inflate(R.layout.fragment_registro_exitoso, container, false)
        volverButton = view.findViewById(R.id.buttonVolver)  // Inicializar el Button
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Controlar la visibilidad del botón
        volverButton?.visibility = if (mostrarVolver) View.VISIBLE else View.GONE

        volverButton?.setOnClickListener {
            onVolverClickListener?.invoke()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        volverButton = null // Limpiar la referencia para evitar fugas de memoria
    }
}