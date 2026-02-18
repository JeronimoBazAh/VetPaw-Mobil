package com.example.vetpaw

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

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
        val cardMascotas = findViewById<CardView>(R.id.cardMascotas)
        val cardTurnos = findViewById<CardView>(R.id.cardTurnos)
        val cardVacunas = findViewById<CardView>(R.id.cardVacunas)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        cardMascotas.setOnClickListener {
            val intent = Intent(this, com.example.vetpaw.ui.mascotas.MascotasActivity::class.java)
            startActivity(intent)
        }

        cardTurnos.setOnClickListener {
            Toast.makeText(this, "Próximamente: Ver Turnos", Toast.LENGTH_SHORT).show()
            // Aquí irás a la pantalla de turnos
        }

        cardVacunas.setOnClickListener {
            Toast.makeText(this, "Próximamente: Próximas Vacunas", Toast.LENGTH_SHORT).show()
            // Aquí irás a la pantalla de vacunas
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
}