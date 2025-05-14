package com.deportes.clubdeportivo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

class EliminarActividadFragment : DialogFragment() {

    private var onVolverClickListener: (() -> Unit)? = null
    private var onEliminarClickListener: (() -> Unit)? = null

    fun setOnVolverClickListener(listener: () -> Unit) {
        onVolverClickListener = listener
    }


    // Seteamos el color de fondo del dialogo a transparente, porque por defecto es gris.
    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_eliminar_actividad, container, false)

        val volverButton = view.findViewById<Button>(R.id.buttonVolver)
        val btnEliminar = view.findViewById<Button>(R.id.buttonEliminar)

        volverButton?.setOnClickListener {
            onVolverClickListener?.invoke()
            dismiss()
        }

        btnEliminar.setOnClickListener {
            onEliminarClickListener?.invoke()
            dismiss()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener referencias a los botones
        val btnVolver = view.findViewById<Button>(R.id.buttonVolver)
        val btnEliminar = view.findViewById<Button>(R.id.buttonEliminar)

        // Configurar el OnClickListener para el botón Volver
        btnVolver.setOnClickListener {
            onVolverClickListener?.invoke()
            dismiss() // Cerrar el diálogo al volver
        }

        // Configurar el OnClickListener para el botón Eliminar
        btnEliminar.setOnClickListener {
            // Notificar que se confirmó la eliminación
            onEliminarClickListener?.invoke()
            dismiss() // Cierra este diálogo después de la confirmación

            // Mostrar el Fragment de éxito
            val eliminadoConExitoDialog = EliminadoConExitoFragment.newInstance()
            eliminadoConExitoDialog.show(
                parentFragmentManager, EliminadoConExitoFragment.TAG
            )
        }
    }

    companion object {
        const val TAG = "EliminarActividadFragment"
        fun newInstance(): EliminarActividadFragment {
            return EliminarActividadFragment()
        }

    }

}