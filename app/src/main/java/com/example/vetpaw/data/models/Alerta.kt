package com.example.vetpaw.data.models

data class Alerta(
    val id: Long,
    val tipo: String,
    val titulo: String,
    val descripcion: String,
    val fechaVencimiento: String,
    val fechaCreacion: String,
    val estado: String,
    val prioridad: String,
    val diasAtraso: Int?,
    val mascotaNombre: String,
    val mascotaId: Long
)