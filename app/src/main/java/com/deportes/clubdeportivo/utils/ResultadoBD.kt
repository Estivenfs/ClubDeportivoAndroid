package com.deportes.clubdeportivo.utils

sealed class ResultadoBD<out T> {
    data class Exito<out T>(val datos: T) : ResultadoBD<T>()
    object YaExiste : ResultadoBD<Nothing>()
    data class Error(val mensaje: String) : ResultadoBD<Nothing>()
    object CamposIncompletos : ResultadoBD<Nothing>()
}