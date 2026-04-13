package com.example.vetpaw.ui.vacunas

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetpaw.R

class VacunasActivity : AppCompatActivity() {

    private val viewModel: VacunasViewModel by viewModels()
    private lateinit var adapter: VacunaAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutVacio: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacunas)

        recyclerView = findViewById(R.id.recyclerVacunas)
        progressBar = findViewById(R.id.progressBar)
        layoutVacio = findViewById(R.id.layoutVacio)

        findViewById<TextView>(R.id.btnVolver).setOnClickListener { finish() }

        adapter = VacunaAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Dentro de VacunasActivity.kt
        val modoCarnet = intent.getStringExtra("FILTRO_ESTADO") == "APLICADA"

        if (modoCarnet) {
            supportActionBar?.title = "Carnet Digital"
            // Aquí le pedimos al ViewModel que solo nos de las aplicadas
            viewModel.vacunas.observe(this) { lista ->
                val filtradas = lista.filter { it.estado == "APLICADA" }
                adapter.updateList(filtradas)
            }
        }

        viewModel.vacunas.observe(this) { vacunas ->
            if (vacunas.isEmpty()) {
                layoutVacio.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                layoutVacio.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                adapter.updateList(vacunas)
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            error?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }

        val prefs = getSharedPreferences("VetPawPrefs", MODE_PRIVATE)
        val token = prefs.getString("token", "") ?: ""
        if (token.isEmpty()) { finish(); return }
        viewModel.cargarVacunas(token)
    }

}