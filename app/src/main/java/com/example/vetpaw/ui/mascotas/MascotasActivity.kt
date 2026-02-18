package com.example.vetpaw.ui.mascotas

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetpaw.R
import com.example.vetpaw.data.models.Mascota
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MascotasActivity : AppCompatActivity() {

    private val viewModel: MascotasViewModel by viewModels()
    private lateinit var adapter: MascotaAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvVacio: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mascotas)

        initViews()
        setupRecyclerView()
        setupObservers()
        cargarMascotas()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerMascotas)
        progressBar = findViewById(R.id.progressBar)
        tvVacio = findViewById(R.id.tvVacio)

        // Botón volver
        val btnVolver = findViewById<TextView>(R.id.btnVolver)
        btnVolver.setOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        adapter = MascotaAdapter(emptyList()) { mascota ->
            mostrarMenuOpciones(mascota)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.mascotas.observe(this) { mascotas ->
            if (mascotas.isEmpty()) {
                tvVacio.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                tvVacio.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                adapter.updateList(mascotas)
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun cargarMascotas() {
        val prefs = getSharedPreferences("VetPawPrefs", MODE_PRIVATE)
        val token = prefs.getString("token", "") ?: ""
        viewModel.cargarMascotas(token)
    }

    private fun mostrarMenuOpciones(mascota: Mascota) {
        val opciones = arrayOf(
            "🐾 Ver Detalle",
            "📋 Historial Médico",
            "💉 Ver Vacunas",
            "❌ Cancelar"
        )

        MaterialAlertDialogBuilder(this)
            .setTitle(mascota.nombre)
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> Toast.makeText(this, "Ver detalle de ${mascota.nombre}", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(this, "Historial de ${mascota.nombre}", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(this, "Vacunas de ${mascota.nombre}", Toast.LENGTH_SHORT).show()
                    3 -> { /* Cancelar */ }
                }
            }
            .show()
    }
}