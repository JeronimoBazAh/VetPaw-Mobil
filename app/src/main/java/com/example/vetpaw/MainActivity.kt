package com.example.vetpaw

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.vetpaw.ui.vacunas.VacunasActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_simple)

        // Obtener token guardado
        val prefs = getSharedPreferences("VetPawPrefs", MODE_PRIVATE)
        val token = prefs.getString("token", null)

        // Configurar bienvenida
        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)
        tvBienvenida.text = "¡Bienvenido!"

        // Configurar clicks de los cards
        val cardCarnetDigital = findViewById<CardView>(R.id.cardCarnetDigital)
        val cardMascotas = findViewById<CardView>(R.id.cardMascotas)
        val cardTurnos = findViewById<CardView>(R.id.cardTurnos)
        val cardVacunas = findViewById<CardView>(R.id.cardVacunas)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        solicitarPermisoNotificaciones()


        cardCarnetDigital.setOnClickListener {
            val intent = Intent(this, SeleccionarMascotaActivity::class.java)
            startActivity(intent)
        }

        cardMascotas.setOnClickListener {
            val intent = Intent(this, com.example.vetpaw.ui.mascotas.MascotasActivity::class.java)
            startActivity(intent)
        }

        cardTurnos.setOnClickListener {
            val intent = Intent(this, com.example.vetpaw.ui.turnos.TurnosActivity::class.java)
            startActivity(intent)
        }

        cardVacunas.setOnClickListener {
            val intent = Intent(this, com.example.vetpaw.ui.vacunas.VacunasActivity::class.java)
            startActivity(intent)
        }

        val cardAlertas = findViewById<CardView>(R.id.cardAlertas)
        cardAlertas.setOnClickListener {
            val intent = Intent(this, com.example.vetpaw.ui.alertas.AlertasActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            // Borrar token
            prefs.edit().clear().apply()

            // Volver al login
            val intent = Intent(this, com.example.vetpaw.ui.login.LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun solicitarPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
    }
}