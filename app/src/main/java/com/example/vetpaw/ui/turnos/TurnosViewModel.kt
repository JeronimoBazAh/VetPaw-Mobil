package com.example.vetpaw.ui.turnos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetpaw.data.api.RetrofitClient
import com.example.vetpaw.data.models.Turno
import kotlinx.coroutines.launch

class TurnosViewModel : ViewModel() {

    private val _turnos = MutableLiveData<List<Turno>>()
    val turnos: LiveData<List<Turno>> = _turnos

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarTodosTurnos(token: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                val resultado = RetrofitClient.api.getMisTurnos(token)
                _turnos.value = resultado

            } catch (e: Exception) {
                _error.value = "Error al cargar turnos: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun cargarTurnosPorMascota(idMascota: Long, token: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null

                val resultado = RetrofitClient.api.getTurnosPorMascota(idMascota, token)
                _turnos.value = resultado

            } catch (e: Exception) {
                _error.value = "Error al cargar turnos: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun cargarTurnosProximos(token: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                val resultado = RetrofitClient.api.getTurnosProximos(token)
                _turnos.value = resultado
            } catch (e: Exception) {
                _error.value = "Error al cargar próximos turnos: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}