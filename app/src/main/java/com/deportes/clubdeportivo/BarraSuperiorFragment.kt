package com.deportes.clubdeportivo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.deportes.clubdeportivo.R

class BarraSuperiorFragment : Fragment() {

    private var title: String? = null
    private var showBackButton: Boolean = true
    private var customBackButtonClickListener: (() -> Unit)? = null

    private lateinit var textViewTitle: TextView
    private lateinit var buttonBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            showBackButton = it.getBoolean(ARG_SHOW_BACK_BUTTON, true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_barra_superior, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewTitle = view.findViewById(R.id.textViewTitle)
        buttonBack = view.findViewById(R.id.buttonBack)

        textViewTitle.text = title ?: "Título"
        buttonBack.visibility = if (showBackButton) View.VISIBLE else View.GONE

        buttonBack.setOnClickListener {
            customBackButtonClickListener?.invoke() ?: activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    fun setTitle(newTitle: String) {
        title = newTitle
        if (::textViewTitle.isInitialized) {
            textViewTitle.text = newTitle
        }
    }

    fun setShowBackButton(show: Boolean) {
        showBackButton = show
        if (::buttonBack.isInitialized) {
            buttonBack.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    fun setOnBackButtonClickListener(listener: () -> Unit) {
        customBackButtonClickListener = listener
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_SHOW_BACK_BUTTON = "showBackButton"

        @JvmStatic
        fun newInstance(title: String, showBackButton: Boolean = true) =
            BarraSuperiorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putBoolean(ARG_SHOW_BACK_BUTTON, showBackButton)
                }
            }
    }
}