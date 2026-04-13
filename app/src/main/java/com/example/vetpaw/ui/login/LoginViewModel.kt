package com.example.vetpaw.ui.login
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetpaw.data.api.LoginRequest
import com.example.vetpaw.data.api.RetrofitClient
import com.example.vetpaw.data.api.VeterinariaApi
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading


    fun login(documento: String, password: String){

        if(documento.isEmpty() || password.isEmpty()){
            _loginResult.value = LoginResult.Error("Por favor completa todos los datos ")
            return
        }

        if(!documento.all {it.isDigit()}){
            _loginResult.value = LoginResult.Error("El documento debe contener solo digitos de tipo numericos ")
            return
        }

        viewModelScope.launch {
            try{
                _loading.value = true
                val response = RetrofitClient.api.login(
                    credentials = LoginRequest(documento, password)
                )
                _loginResult.value = LoginResult.Success(response.token, response.usuario)

            }catch (e: Exception){
                _loginResult.value = LoginResult.Error("Error: ${e.message}")
            }finally {
                _loading.value = false
            }

        }
}



    sealed class LoginResult {
        data class Success(val token: String, val usuario: String) : LoginResult()
        data class Error(val message: String) : LoginResult()
    }
}