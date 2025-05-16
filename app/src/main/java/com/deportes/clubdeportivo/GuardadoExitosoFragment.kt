package com.deportes.clubdeportivo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

class GuardadoExitosoFragment : DialogFragment() {

    private var onVolverClickListener: (() -> Unit)? = null

    fun setOnVolverClickListener(listener: () -> Unit) {
        onVolverClickListener = listener
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_guardado_exitoso, container, false)

        val volverButton = view.findViewById<Button>(R.id.buttonVolver)
        volverButton.setOnClickListener {
            onVolverClickListener?.invoke()
            dismiss()
        }

        return view
    }

    companion object {
        const val TAG = "FragmentGuardadoExitoso"
        fun newInstance(): GuardadoExitosoFragment {
            return GuardadoExitosoFragment()
        }
    }
}
