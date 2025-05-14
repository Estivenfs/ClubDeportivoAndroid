package com.deportes.clubdeportivo

// DatePickerUtils.kt


import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

object DatePickerUtils {
    fun mostrarDatePickerDialog(context: Context, textView: TextView) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(context,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                    Calendar.getInstance().apply {
                        set(year, monthOfYear, dayOfMonth)
                    }.time
                )
                textView.text = selectedDate
            }, year, month, day
        )
        dpd.show()
    }
}
