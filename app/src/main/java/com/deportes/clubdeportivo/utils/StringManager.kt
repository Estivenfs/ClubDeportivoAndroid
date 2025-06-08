package com.deportes.clubdeportivo.utils

import java.util.Locale

class StringManager {
    fun capitalizeWords(text: String) : String{
        if (text.isEmpty()) {
            return text
        }

        // Dividir la cadena en palabras, capitalizar cada una y unirlas de nuevo
        return text.split(" ") // Divide la cadena por cada espacio
            .joinToString(" ") { word -> // Une las palabras de nuevo con un espacio
                if (word.isEmpty()) {
                    word // Si una "palabra" es un espacio extra, la mantiene
                } else {
                    // Capitaliza la primera letra y pone el resto en minúsculas
                    word.lowercase(Locale.getDefault()).replaceFirstChar { it.titlecase(Locale.getDefault()) }
                }
            }
    }
}