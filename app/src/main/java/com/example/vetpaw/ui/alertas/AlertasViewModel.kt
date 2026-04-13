package com.example.vetpaw.ui.alertas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.vetpaw.data.api.RetrofitClient
import com.example.vetpaw.data.models.Alerta
import com.example.vetpaw.notifications.NotificationHelper
import kotlinx.coroutines.launch

class AlertasViewModel(application: Application) : AndroidViewModel(application) {

    private val _alertas = MutableLiveData<List<Alerta>>()
    val alertas: LiveData<List<Alerta>> = _alertas

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _countPendientes = MutableLiveData<Long>()
    val countPendientes: LiveData<Long> = _countPendientes

    private val notificationHelper = NotificationHelper(application)

    fun cargarAlertas(token: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                println("=== Llamando API getMisAlertas ===")
                val resultado = RetrofitClient.api.getMisAlertas(token)
                println("=== Alertas recibidas: ${resultado.size} ===")

                // Detectar si hay alertas nuevas
                val alertasAnteriores = _alertas.value?.size ?: 0
                val alertasNuevas = resultado.size

                if (alertasNuevas > alertasAnteriores && alertasAnteriores > 0) {
                    val diff = alertasNuevas - alertasAnteriores
                    notificationHelper.mostrarNotificacionConContador(
                        "Nuevas alertas",
                        "Tienes $diff nueva(s) alerta(s) pendiente(s)",
                        diff
                    )
                }

                _alertas.value = resultado

            } catch (e: Exception) {
                println("=== Error al cargar alertas: ${e.message} ===")
                e.printStackTrace()
                _error.value = "Error al cargar alertas: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun cargarCountPendientes(token: String) {
        viewModelScope.launch {
            try {
                println("=== Cargando count pendientes ===")
                val resultado = RetrofitClient.api.getCountAlertasPendientes(token)
                _countPendientes.value = resultado.count
                println("=== Count pendientes: ${resultado.count} ===")
            } catch (e: Exception) {
                println("=== Error al cargar count: ${e.message} ===")
                _countPendientes.value = 0
            }
        }
    }

    fun marcarComoVista(alertaId: Long, token: String) {
        viewModelScope.launch {
            try {
                println("=== Marcando alerta $alertaId como vista ===")
                RetrofitClient.api.marcarAlertaComoVista(alertaId)
                cargarAlertas(token) // Recargar lista
                cargarCountPendientes(token) // Actualizar contador
                println("=== Alerta marcada como vista ===")
            } catch (e: Exception) {
                println("=== Error al marcar como vista: ${e.message} ===")
                _error.value = "Error al actualizar: ${e.message}"
            }
        }
    }

    fun marcarComoCompletada(alertaId: Long, token: String) {
        viewModelScope.launch {
            try {
                println("=== Marcando alerta $alertaId como completada ===")
                RetrofitClient.api.marcarAlertaComoCompletada(alertaId)
                cargarAlertas(token) // Recargar lista
                cargarCountPendientes(token) // Actualizar contador
                println("=== Alerta marcada como completada ===")
            } catch (e: Exception) {
                println("=== Error al marcar como completada: ${e.message} ===")
                _error.value = "Error al actualizar: ${e.message}"
            }
        }
    }
}