package com.example.vetpaw.ui.vacunas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetpaw.data.api.RetrofitClient
import com.example.vetpaw.data.models.Vacuna
import kotlinx.coroutines.launch

class VacunasViewModel : ViewModel() {

    private val _vacunas = MutableLiveData<List<Vacuna>>()
    val vacunas: LiveData<List<Vacuna>> = _vacunas

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarVacunas(token: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _error.value = null
                val resultado = RetrofitClient.api.getMisVacunas(token)
                _vacunas.value = resultado
            } catch (e: Exception) {
                _error.value = "Error al cargar vacunas: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}