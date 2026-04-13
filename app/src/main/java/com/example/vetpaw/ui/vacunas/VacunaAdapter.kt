package com.example.vetpaw.ui.vacunas

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetpaw.R
import com.example.vetpaw.data.models.Vacuna

class VacunaAdapter(
    private var vacunas: List<Vacuna>
) : RecyclerView.Adapter<VacunaAdapter.VacunaViewHolder>() {

    inner class VacunaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreVacuna: TextView = itemView.findViewById(R.id.tvNombreVacuna)
        val tvMascota: TextView = itemView.findViewById(R.id.tvMascota)
        val tvFechaProxima: TextView = itemView.findViewById(R.id.tvFechaProxima)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacunaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vacuna, parent, false)
        return VacunaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VacunaViewHolder, position: Int) {
        val vacuna = vacunas[position]

        // 1. Nombre de la Vacuna
        holder.tvNombreVacuna.text = vacuna.nombreVacuna

        // 2. Nombre de la Mascota
        holder.tvMascota.text = "🐾 ${vacuna.mascotaNombre}"

        // 3. Estado con manejo de nulos
        val estadoActual = vacuna.estado?.uppercase() ?: "SIN_DATO"
        holder.tvEstado.text = estadoActual

        // Colores del estado
        when (estadoActual) {
            "APLICADA"  -> holder.tvEstado.setBackgroundColor(Color.parseColor("#4CAF50"))
            "PENDIENTE" -> holder.tvEstado.setBackgroundResource(R.drawable.chip_purple)
            "VENCIDA"   -> holder.tvEstado.setBackgroundColor(Color.parseColor("#F44336"))
            else        -> holder.tvEstado.setBackgroundColor(Color.parseColor("#9E9E9E"))
        }
        holder.tvEstado.setTextColor(Color.WHITE)

        // 4. LÓGICA PARA EL CARNET (Solo nombre y fecha aplicación)
        if (estadoActual == "APLICADA") {
            // Mostramos la fecha de aplicación y ocultamos/vaciamos lo que no queremos
            val fechaApp = vacuna.fechaAplicacion?.let { formatearFecha(it) } ?: "--/--/----"
            holder.tvFechaProxima.text = "Aplicada el: $fechaApp"

            // Si quieres que el texto sea más grande o de otro color para que destaque:
            holder.tvFechaProxima.setTextColor(Color.BLACK)
        } else {
            // Si es PENDIENTE o VENCIDA, mostramos la próxima como estaba antes
            val fechaProx = vacuna.fechaProxima?.let { formatearFecha(it) } ?: "Sin fecha"
            holder.tvFechaProxima.text = "Próxima: $fechaProx"
            holder.tvFechaProxima.setTextColor(Color.GRAY)
        }
    }

    override fun getItemCount() = vacunas.size

    fun updateList(nuevas: List<Vacuna>) {
        vacunas = nuevas
        notifyDataSetChanged()
    }

    private fun formatearFecha(fecha: String?): String {
        if (fecha.isNullOrEmpty()) return "--/--/----"
        return try {
            val p = fecha.split("-")
            if (p.size >= 3) "${p[2]}/${p[1]}/${p[0]}" else fecha
        } catch (e: Exception) {
            fecha
        }
    }
}