package com.example.vetpaw.ui.mascotas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetpaw.data.api.RetrofitClient
import com.example.vetpaw.data.models.Mascota
import kotlinx.coroutines.launch

class MascotasViewModel : ViewModel(){

    private val _mascotas = MutableLiveData<List<Mascota>>()
    val mascotas: LiveData<List<Mascota>> = _mascotas

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun cargarMascotas(token: String){
        viewModelScope.launch {
            try{
                _loading.value = true
                _error.value = null

                val resultado = RetrofitClient.api.getMisMascotas("Bearer $token")
                _mascotas.value = resultado
            } catch (e: Exception){
                _error.value = "Error al cargar mascotas: ${e.message}"
            }finally {
                _loading.value = false

            }
        }
    }





}