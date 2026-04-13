package com.example.vetpaw.data.api

import com.example.vetpaw.data.models.Alerta
import com.example.vetpaw.data.models.Mascota
import com.example.vetpaw.data.models.Turno
import com.example.vetpaw.data.models.Vacuna
import retrofit2.http.*

interface VeterinariaApi {

    @GET("api/mascotas/mis-mascotas")
    suspend fun getMisMascotas(
        @Header("Authorization") token: String
    ): List<Mascota>

    @GET("api/turnos/mis-turnos")
    suspend fun getMisTurnos(
        @Header("Authorization") token: String
    ): List<Turno>

    @GET("api/turnos/mascota/{idMascota}")
    suspend fun getTurnosPorMascota(
        @Path("idMascota") idMascota: Long,
        @Header("Authorization") token: String
    ): List<Turno>

    // NUEVO: Endpoints de alertas
    @GET("api/alertas/mis-alertas")
    suspend fun getMisAlertas(
        @Header("Authorization") token: String
    ): List<Alerta>

    @GET("api/alertas/count-pendientes")
    suspend fun getCountAlertasPendientes(
        @Header("Authorization") token: String
    ): CountResponse

    @PUT("api/alertas/{id}/marcar-vista")
    suspend fun marcarAlertaComoVista(
        @Path("id") alertaId: Long
    ): SuccessResponse

    @PUT("api/alertas/{id}/marcar-completada")
    suspend fun marcarAlertaComoCompletada(
        @Path("id") alertaId: Long
    ): SuccessResponse

    @POST("api/auth/login")
    suspend fun login(
        @Body credentials: LoginRequest
    ): LoginResponse

    @GET("api/turnos/proximos")
    suspend fun getTurnosProximos(
        @Header("Authorization") token: String
    ): List<Turno>

    @GET("api/vacunas/mis-vacunas")
    suspend fun getMisVacunas(
        @Header("Authorization") token: String
    ): List<Vacuna>

    @GET("api/vacunas/historial")
    suspend fun getCarnetDigital(
        @Header("Authorization") token: String
    ): List<Vacuna>
}

data class LoginRequest(
    val documento: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val usuario: String
)

data class CountResponse(
    val count: Long
)

data class SuccessResponse(
    val message: String
)