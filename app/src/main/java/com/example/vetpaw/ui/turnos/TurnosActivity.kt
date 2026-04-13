package com.example.vetpaw.ui.turnos

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetpaw.R
import com.example.vetpaw.data.models.Turno
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TurnosActivity : AppCompatActivity() {

    private val viewModel: TurnosViewModel by viewModels()
    private lateinit var adapter: TurnoAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutVacio: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turnos)

        initViews()
        setupRecyclerView()
        setupObservers()
        cargarTurnos()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerTurnos)
        progressBar = findViewById(R.id.progressBar)
        layoutVacio = findViewById(R.id.layoutVacio)

        val btnVolver = findViewById<TextView>(R.id.btnVolver)
        btnVolver.setOnClickListener { finish() }

        val btnTodos = findViewById<Button>(R.id.btnTodos)
        val btnProximos = findViewById<Button>(R.id.btnProximos)

        btnTodos.setOnClickListener {
            cargarTurnos()
        }

        btnProximos.setOnClickListener {
            val prefs = getSharedPreferences("VetPawPrefs", MODE_PRIVATE)
            val token = prefs.getString("token", "") ?: ""
            if (token.isEmpty()) {
                Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show()
                finish()
                return@setOnClickListener
            }
            viewModel.cargarTurnosProximos(token)
            // Resaltar botón activo
            btnProximos.setBackgroundResource(R.drawable.button_purple)
            btnTodos.setBackgroundResource(R.drawable.button_purple) // o el drawable que uses para inactivo
        }

        btnTodos.setOnClickListener {
            cargarTurnos()
            btnTodos.setBackgroundResource(R.drawable.button_purple)
            btnProximos.setBackgroundResource(R.drawable.button_purple)
        }
    }

    private fun setupRecyclerView() {
        adapter = TurnoAdapter(emptyList()) { turno ->
            mostrarDetalleTurno(turno)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.turnos.observe(this) { turnos ->
            if (turnos.isEmpty()) {
                layoutVacio.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                layoutVacio.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                adapter.updateList(turnos)
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

    private fun cargarTurnos() {
        val prefs = getSharedPreferences("VetPawPrefs", MODE_PRIVATE)
        val token = prefs.getString("token", "") ?: ""

        if (token.isEmpty()) {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel.cargarTodosTurnos(token)
    }

    private fun mostrarDetalleTurno(turno: Turno) {
        val mensaje = """
            Mascota: ${turno.mascotaNombre}
            Fecha: ${turno.fecha}
            Hora: ${turno.hora}
            Estado: ${turno.estado}
            Veterinario: ${turno.veterinarioNombre ?: "No asignado"}
            Motivo: ${turno.motivo ?: "Sin especificar"}
        """.trimIndent()

        MaterialAlertDialogBuilder(this)
            .setTitle("Detalle del Turno")
            .setMessage(mensaje)
            .setPositiveButton("Cerrar", null)
            .show()
    }
}