package com.example.vetpaw.ui.mascotas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetpaw.R
import com.example.vetpaw.data.models.Mascota

class MascotaAdapter(
    private var mascotas: List<Mascota>,
    private val onItemClick: (Mascota) -> Unit
) : RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder>() {

    inner class MascotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgMascota: ImageView = itemView.findViewById(R.id.imgMascota)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvEspecieRaza: TextView = itemView.findViewById(R.id.tvEspecieRaza)
        val tvEdad: TextView = itemView.findViewById(R.id.tvEdad)
        val tvPeso: TextView = itemView.findViewById(R.id.tvPeso)
        val tvColor: TextView = itemView.findViewById(R.id.tvColor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MascotaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mascota, parent, false)
        return MascotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MascotaViewHolder, position: Int) {
        val mascota = mascotas[position]

        holder.tvNombre.text = mascota.nombre
        holder.tvEspecieRaza.text = "${mascota.especie} - ${mascota.raza}"
        holder.tvEdad.text = "${mascota.edad} años"
        holder.tvPeso.text = "${mascota.peso} kg"
        holder.tvColor.text = mascota.color

        // Click en el item
        holder.itemView.setOnClickListener {
            onItemClick(mascota)
        }
    }

    override fun getItemCount() = mascotas.size

    // Actualizar la lista
    fun updateList(nuevasMascotas: List<Mascota>) {
        mascotas = nuevasMascotas
        notifyDataSetChanged()
    }
}