package com.example.vetpaw.ui.turnos

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetpaw.R
import com.example.vetpaw.data.models.Turno
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class TurnoAdapter(
    private var turnos: List<Turno>,
    private val onItemClick: (Turno) -> Unit
) : RecyclerView.Adapter<TurnoAdapter.TurnoViewHolder>() {

    inner class TurnoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMascotaNombre: TextView = itemView.findViewById(R.id.tvMascotaNombre)
        val tvFechaHora: TextView = itemView.findViewById(R.id.tvFechaHora)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        val tvVeterinario: TextView = itemView.findViewById(R.id.tvVeterinario)
        val tvMotivo: TextView = itemView.findViewById(R.id.tvMotivo)
        val layoutMotivo: LinearLayout = itemView.findViewById(R.id.layoutMotivo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TurnoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_turno, parent, false)
        return TurnoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TurnoViewHolder, position: Int) {
        val turno = turnos[position]

        holder.tvMascotaNombre.text = turno.mascotaNombre

        // Formatear fecha y hora
        try {
            val fechaFormateada = formatearFecha(turno.fecha)
            val horaFormateada = formatearHora(turno.hora)
            holder.tvFechaHora.text = "$fechaFormateada - $horaFormateada"
        } catch (e: Exception) {
            holder.tvFechaHora.text = "${turno.fecha} - ${turno.hora}"
        }
        try {
            val partes = turno.fecha.split("-")
            val fechaTurno = LocalDate.of(partes[0].toInt(), partes[1].toInt(), partes[2].toInt())
            val hoy = LocalDate.now()
            val diasRestantes = ChronoUnit.DAYS.between(hoy, fechaTurno)

            when {
                diasRestantes < 0 -> { /* pasado, sin badge */ }
                diasRestantes == 0L -> holder.tvFechaHora.text = "HOY - ${holder.tvFechaHora.text}"
                diasRestantes == 1L -> holder.tvFechaHora.text = "MAÑANA - ${holder.tvFechaHora.text}"
                diasRestantes <= 7 -> holder.tvFechaHora.text = "En $diasRestantes días - ${holder.tvFechaHora.text}"
            }
        } catch (e: Exception) { /* ignorar */ }
        // Estado con colores
        holder.tvEstado.text = turno.estado
        when (turno.estado.uppercase()) {
            "PROGRAMADO" -> {
                holder.tvEstado.setBackgroundResource(R.drawable.chip_purple)
                holder.tvEstado.setTextColor(Color.WHITE)
            }
            "COMPLETADO" -> {
                holder.tvEstado.setBackgroundColor(Color.parseColor("#4CAF50"))
                holder.tvEstado.setTextColor(Color.WHITE)
            }
            "CANCELADO" -> {
                holder.tvEstado.setBackgroundColor(Color.parseColor("#F44336"))
                holder.tvEstado.setTextColor(Color.WHITE)
            }
            else -> {
                holder.tvEstado.setBackgroundColor(Color.parseColor("#9E9E9E"))
                holder.tvEstado.setTextColor(Color.WHITE)
            }
        }

        // Veterinario
        holder.tvVeterinario.text = turno.veterinarioNombre ?: "No asignado"

        // Motivo (opcional)
        if (turno.motivo.isNullOrBlank()) {
            holder.layoutMotivo.visibility = View.GONE
        } else {
            holder.layoutMotivo.visibility = View.VISIBLE
            holder.tvMotivo.text = turno.motivo
        }

        // Click
        holder.itemView.setOnClickListener {
            onItemClick(turno)
        }
    }

    override fun getItemCount() = turnos.size

    fun updateList(nuevosTurnos: List<Turno>) {
        turnos = nuevosTurnos
        notifyDataSetChanged()
    }

    private fun formatearFecha(fecha: String): String {
        return try {
            // Convertir de "2026-02-24" a "24/02/2026"
            val partes = fecha.split("-")
            "${partes[2]}/${partes[1]}/${partes[0]}"
        } catch (e: Exception) {
            fecha
        }
    }

    private fun formatearHora(hora: String): String {
        return try {
            // Extraer solo HH:mm de "2026-02-24T15:30:00"
            val timePart = hora.split("T").getOrNull(1) ?: hora
            timePart.substring(0, 5) // "15:30"
        } catch (e: Exception) {
            hora
        }
    }
}