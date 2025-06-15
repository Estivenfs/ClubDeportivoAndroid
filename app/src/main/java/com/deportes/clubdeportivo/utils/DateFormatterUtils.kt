package com.deportes.clubdeportivo.utils

import java.text.SimpleDateFormat
import java.util.*

sealed class DateFormatResult {
    data class Success(val formattedDate: String) : DateFormatResult()
    data class Error(val errorMessage: String) : DateFormatResult()
}

class DateFormatter {
    fun formatInputDateForDatabase(inputDateString: String): DateFormatResult {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return try {
            val date = inputFormat.parse(inputDateString)
            if (date != null) {
                val formattedDate = outputFormat.format(date)
                DateFormatResult.Success(formattedDate)
            } else {
                DateFormatResult.Error("No se pudo parsear la fecha (resultado nulo) para: $inputDateString")
            }
        } catch (e: Exception) {
            DateFormatResult.Error("Formato de fecha inválido. Se esperaba 'dd/MM/yyyy'. Error: ${e.message}")
        }
    }
}