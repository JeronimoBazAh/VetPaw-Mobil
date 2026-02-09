package com.example.vetpaw.data.api
import com.example.vetpaw.data.models.Mascota
import retrofit2.http.*
interface VeterinariaAPI {

    @GET("api/mascotas/mis-mascotas")
    suspend fun getMisMascotas(
        @Header("Authorization") token: String
    ): List<Mascota>

    // Obtener una mascota específica
    @GET("api/mascotas/{id}")
    suspend fun getMascota(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Mascota

    // Login (para obtener el token)
    @POST("api/auth/login")
    suspend fun login(
        @Body credentials: LoginRequest
    ): LoginResponse

    data class LoginRequest(
        val documento: String,
        val password: String
    )

    data class LoginResponse(
        val token: String,
        val usuario: String
    )
}