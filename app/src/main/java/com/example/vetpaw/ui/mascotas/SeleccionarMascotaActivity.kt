import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetpaw.R
import com.example.vetpaw.ui.mascotas.MascotaAdapter
import com.example.vetpaw.ui.mascotas.MascotasViewModel
import com.example.vetpaw.ui.vacunas.VacunasActivity
import kotlin.getValue

// SeleccionarMascotaActivity.kt
class SeleccionarMascotaActivity : AppCompatActivity() {

    private lateinit var adapter: MascotaAdapter
    private val viewModel: MascotasViewModel by viewModels() // Asumo que tienes un ViewModel para mascotas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionar_mascota)

        supportActionBar?.title = "Selecciona tu Mascota"

        val rv = findViewById<RecyclerView>(R.id.rvMascotasSeleccion)
        rv.layoutManager = LinearLayoutManager(this)

        // Aquí es donde sucede la magia del click
        adapter = MascotaAdapter(emptyList()) { mascota ->
            val intent = Intent(this, VacunasActivity::class.java)
            intent.putExtra("ID_MASCOTA", mascota.id)
            intent.putExtra("NOMBRE_MASCOTA", mascota.nombre)
            intent.putExtra("SOLO_APLICADAS", true) // Para que sepa que es el Carnet
            startActivity(intent)
        }

        rv.adapter = adapter

        // Observamos las mascotas (esto depende de cómo tengas tu ViewModel)
        val token = getSharedPreferences("pref", MODE_PRIVATE).getString("token", "") ?: ""
        viewModel.cargarMascotas(token)

        viewModel.mascotas.observe(this) { lista ->
            adapter.updateList(lista)
        }
    }
}