package com.deportes.clubdeportivo.utils


import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deportes.clubdeportivo.R
import com.deportes.clubdeportivo.adapters.OptionAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

fun AppCompatActivity.setBottomSheetSelector(
    layout: LinearLayout,
    options: List<String>,
    title: String,
    textView: TextView
) {
    layout.setOnClickListener {
        showBottomSheetSelector(title, options) { seleccion ->
            textView.text = seleccion
        }
    }
}

fun Context.showBottomSheetSelector(
    title: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    val dialog = BottomSheetDialog(this)
    val view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_selector, null)

    val titleText = view.findViewById<TextView>(R.id.titleBottomSheet)
    val recyclerView = view.findViewById<RecyclerView>(R.id.listOptions)

    titleText.text = title
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = OptionAdapter(options) {
        onSelected(it)
        dialog.dismiss()
    }

    dialog.setContentView(view)
    dialog.show()
}
