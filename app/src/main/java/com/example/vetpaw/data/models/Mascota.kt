package com.example.vetpaw.data.models

data class Mascota(
    val id: Long,
    val nombre: String,
    val especie: String,
    val raza: String,
    val edad: Int,
    val peso: Double,
    val color: String,
    val fotoUrl: String? = null
)