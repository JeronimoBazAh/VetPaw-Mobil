package com.example.vetpaw.data.models

data class Vacuna(
    val id: Long,
    val nombreVacuna: String,
    val fechaAplicacion: String?,
    val fechaProxima: String?,
    val mascotaNombre: String,
    val estado: String?
)