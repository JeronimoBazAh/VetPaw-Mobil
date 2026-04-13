package com.example.vetpaw.ui.alertas

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetpaw.R
import com.example.vetpaw.data.models.Alerta

class AlertaAdapter(
    private var alertas: List<Alerta>,
    private val onItemClick: (Alerta) -> Unit
) : RecyclerView.Adapter<AlertaAdapter.AlertaViewHolder>() {

    inner class AlertaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewPrioridad: View = itemView.findViewById(R.id.viewPrioridad)
        val imgIconoTipo: ImageView = itemView.findViewById(R.id.imgIconoTipo)
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val tvMascota: TextView = itemView.findViewById(R.id.tvMascota)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        val tvAtraso: TextView = itemView.findViewById(R.id.tvAtraso)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alerta, parent, false)
        return AlertaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertaViewHolder, position: Int) {
        val alerta = alertas[position]

        // Título y mascota
        holder.tvTitulo.text = alerta.titulo
        holder.tvMascota.text = "🐾 ${alerta.mascotaNombre}"
        holder.tvDescripcion.text = alerta.descripcion

        // Fecha
        holder.tvFecha.text = "📅 ${formatearFecha(alerta.fechaVencimiento)}"

        // Días de atraso
        if (alerta.diasAtraso != null && alerta.diasAtraso > 0) {
            holder.tvAtraso.visibility = View.VISIBLE
            holder.tvAtraso.text = "⚠️ ${alerta.diasAtraso} días atrasado"
        } else {
            holder.tvAtraso.visibility = View.GONE
        }

        // Estado
        holder.tvEstado.text = alerta.estado
        when (alerta.estado) {
            "PENDIENTE" -> {
                holder.tvEstado.setBackgroundColor(Color.parseColor("#7B2CBF"))
                holder.tvEstado.setTextColor(Color.WHITE)
            }
            "VISTA" -> {
                holder.tvEstado.setBackgroundColor(Color.parseColor("#9E9E9E"))
                holder.tvEstado.setTextColor(Color.WHITE)
            }
            "COMPLETADA" -> {
                holder.tvEstado.setBackgroundColor(Color.parseColor("#4CAF50"))
                holder.tvEstado.setTextColor(Color.WHITE)
            }
            "VENCIDA" -> {
                holder.tvEstado.setBackgroundColor(Color.parseColor("#F44336"))
                holder.tvEstado.setTextColor(Color.WHITE)
            }
        }

        // Prioridad (barra lateral)
        when (alerta.prioridad) {
            "URGENTE" -> holder.viewPrioridad.setBackgroundColor(Color.parseColor("#D32F2F"))
            "ALTA" -> holder.viewPrioridad.setBackgroundColor(Color.parseColor("#F44336"))
            "MEDIA" -> holder.viewPrioridad.setBackgroundColor(Color.parseColor("#FF9800"))
            "BAJA" -> holder.viewPrioridad.setBackgroundColor(Color.parseColor("#4CAF50"))
        }

        // Icono según tipo
        when (alerta.tipo) {
            "VACUNA" -> holder.imgIconoTipo.setImageResource(android.R.drawable.ic_menu_info_details)
            "CONTROL" -> holder.imgIconoTipo.setImageResource(android.R.drawable.ic_menu_my_calendar)
            "TRATAMIENTO" -> holder.imgIconoTipo.setImageResource(android.R.drawable.ic_menu_add)
            "DESPARASITACION" -> holder.imgIconoTipo.setImageResource(android.R.drawable.ic_menu_help)
            "MEDICACION" -> holder.imgIconoTipo.setImageResource(android.R.drawable.ic_menu_agenda)
        }

        // Click
        holder.itemView.setOnClickListener {
            onItemClick(alerta)
        }
    }

    override fun getItemCount() = alertas.size

    fun updateList(nuevasAlertas: List<Alerta>) {
        alertas = nuevasAlertas
        notifyDataSetChanged()
    }

    private fun formatearFecha(fecha: String): String {
        return try {
            val partes = fecha.split("-")
            "${partes[2]}/${partes[1]}/${partes[0]}"
        } catch (e: Exception) {
            fecha
        }
    }
}