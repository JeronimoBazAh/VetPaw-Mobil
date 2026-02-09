package com.example.vetpaw.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.vetpaw.MainActivity
import com.example.vetpaw.R
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var etDocumento: TextInputEditText  // ← Cambio aquí
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var tvError: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
        setupObservers()
        setupListeners()
    }

    private fun initViews() {
        etDocumento = findViewById(R.id.etDocumento)  // ← Cambio aquí
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvError = findViewById(R.id.tvError)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupObservers() {
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is LoginViewModel.LoginResult.Success -> {
                    guardarToken(result.token)

                    Toast.makeText(this, "Bienvenido ${result.usuario}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is LoginViewModel.LoginResult.Error -> {
                    mostrarError(result.message)
                }
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            btnLogin.isEnabled = !isLoading
        }
    }

    private fun setupListeners() {
        btnLogin.setOnClickListener {
            val documento = etDocumento.text.toString()  // ← Cambio aquí
            val password = etPassword.text.toString()

            tvError.visibility = View.GONE
            viewModel.login(documento, password)  // ← Cambio aquí
        }
    }

    private fun mostrarError(mensaje: String) {
        tvError.text = mensaje
        tvError.visibility = View.VISIBLE
    }

    private fun guardarToken(token: String) {
        val prefs = getSharedPreferences("VetPawPrefs", MODE_PRIVATE)
        prefs.edit().putString("token", token).apply()
    }
}