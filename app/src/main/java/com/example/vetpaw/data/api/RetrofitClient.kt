package com.example.vetpaw.data.api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"
    // Nota: 10.0.2.2 es la IP para conectar desde el emulador a localhost
    // Si usas teléfono real, usa tu IP local (ejemplo: http://192.168.1.100:8080/)
    val api: VeterinariaAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VeterinariaAPI::class.java)
    }

}