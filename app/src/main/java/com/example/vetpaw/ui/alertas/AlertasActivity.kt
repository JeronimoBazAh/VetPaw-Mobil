package com.example.vetpaw.ui.alertas

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetpaw.R
import com.example.vetpaw.data.models.Alerta

class AlertasActivity : AppCompatActivity() {

    private val viewModel: AlertasViewModel by viewModels()
    private lateinit var adapter: AlertaAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutVacio: LinearLayout
    private lateinit var tvBadgeCount: TextView

    private var todasLasAlertas: List<Alerta> = emptyList()
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alertas)

        initViews()
        setupRecyclerView()
        setupObservers()
        setupFiltros()
        cargarAlertas()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerAlertas)
        progressBar = findViewById(R.id.progressBar)
        layoutVacio = findViewById(R.id.layoutVacio)
        tvBadgeCount = findViewById(R.id.tvBadgeCount)

        // Botón volver
        val btnVolver = findViewById<TextView>(R.id.btnVolver)
        btnVolver.setOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        adapter = AlertaAdapter(emptyList()) { alerta ->
            mostrarMenuOpciones(alerta)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.alertas.observe(this) { alertas ->
            todasLasAlertas = alertas

            if (alertas.isEmpty()) {
                layoutVacio.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                layoutVacio.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                adapter.updateList(alertas)
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

        viewModel.countPendientes.observe(this) { count ->
            tvBadgeCount.text = count.toString()
            if (count > 0) {
                tvBadgeCount.visibility = View.VISIBLE
            } else {
                tvBadgeCount.visibility = View.GONE
            }
        }
    }

    private fun setupFiltros() {
        val chipTodas = findViewById<TextView>(R.id.chipTodas)
        val chipPendientes = findViewById<TextView>(R.id.chipPendientes)
        val chipVacunas = findViewById<TextView>(R.id.chipVacunas)
        val chipControles = findViewById<TextView>(R.id.chipControles)

        chipTodas.setOnClickListener {
            aplicarFiltro("TODAS")
            seleccionarChip(chipTodas, chipPendientes, chipVacunas, chipControles)
        }

        chipPendientes.setOnClickListener {
            aplicarFiltro("PENDIENTE")
            seleccionarChip(chipPendientes, chipTodas, chipVacunas, chipControles)
        }

        chipVacunas.setOnClickListener {
            aplicarFiltro("VACUNA")
            seleccionarChip(chipVacunas, chipTodas, chipPendientes, chipControles)
        }

        chipControles.setOnClickListener {
            aplicarFiltro("CONTROL")
            seleccionarChip(chipControles, chipTodas, chipPendientes, chipVacunas)
        }
    }

    private fun aplicarFiltro(filtro: String) {
        val alertasFiltradas = when (filtro) {
            "TODAS" -> todasLasAlertas
            "PENDIENTE" -> todasLasAlertas.filter { it.estado == "PENDIENTE" }
            "VACUNA" -> todasLasAlertas.filter { it.tipo == "VACUNA" }
            "CONTROL" -> todasLasAlertas.filter { it.tipo == "CONTROL" }
            else -> todasLasAlertas
        }

        adapter.updateList(alertasFiltradas)

        if (alertasFiltradas.isEmpty()) {
            layoutVacio.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            layoutVacio.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun seleccionarChip(seleccionado: TextView, vararg otros: TextView) {
        // Chip seleccionado
        seleccionado.setBackgroundResource(R.drawable.chip_purple)
        seleccionado.setTextColor(getColor(android.R.color.white))

        // Chips no seleccionados
        for (chip in otros) {
            chip.setBackgroundResource(R.drawable.chip_purple_outline)
            chip.setTextColor(getColor(R.color.purple_500))
        }
    }

    private fun cargarAlertas() {
        val prefs = getSharedPreferences("VetPawPrefs", MODE_PRIVATE)
        token = prefs.getString("token", "") ?: ""

        if (token.isEmpty()) {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel.cargarAlertas(token)
        viewModel.cargarCountPendientes(token)
    }

    private fun mostrarMenuOpciones(alerta: Alerta) {
        val opciones = mutableListOf<String>()

        if (alerta.estado == "PENDIENTE") {
            opciones.add("✓ Marcar como vista")
            opciones.add("✓ Marcar como completada")
        }

        opciones.add("ℹ️ Ver detalle")
        opciones.add("❌ Cancelar")

        AlertDialog.Builder(this)
            .setTitle(alerta.titulo)
            .setItems(opciones.toTypedArray()) { _, which ->
                when {
                    alerta.estado == "PENDIENTE" && which == 0 -> {
                        // Marcar como vista
                        viewModel.marcarComoVista(alerta.id, token)
                        Toast.makeText(this, "Marcada como vista", Toast.LENGTH_SHORT).show()
                    }
                    alerta.estado == "PENDIENTE" && which == 1 -> {
                        // Marcar como completada
                        viewModel.marcarComoCompletada(alerta.id, token)
                        Toast.makeText(this, "Marcada como completada", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // Ver detalle o cancelar
                        if (opciones[which].startsWith("ℹ️")) {
                            mostrarDetalleAlerta(alerta)
                        }
                    }
                }
            }
            .show()
    }

    private fun mostrarDetalleAlerta(alerta: Alerta) {
        val mensaje = """
            Mascota: ${alerta.mascotaNombre}
            Tipo: ${alerta.tipo}
            Estado: ${alerta.estado}
            Prioridad: ${alerta.prioridad}
            Fecha vencimiento: ${alerta.fechaVencimiento}
            ${if (alerta.diasAtraso != null) "Días de atraso: ${alerta.diasAtraso}" else ""}
            
            ${alerta.descripcion}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle(alerta.titulo)
            .setMessage(mensaje)
            .setPositiveButton("Cerrar", null)
            .show()
    }
}