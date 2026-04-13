package com.example.vetpaw.data.models

data class Turno (
    val idTurno: Long,
    val estado: String,
    val fecha: String,
    val hora: String,
    val motivo: String?,
    val veterinarioNombre: String?,
    val mascotaNombre: String,
    val mascotaId: Long
)