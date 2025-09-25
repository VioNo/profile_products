package com.example.zadanie2004

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        // Находим View по id
        val tvName = findViewById<TextView>(R.id.tvName)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val btnBack = findViewById<Button>(R.id.btnBack)

        // Получаем Intent, который запустил эту Activity, и извлекаем данные
        val receivedName = intent.getStringExtra("EXTRA_NAME")
        val receivedEmail = intent.getStringExtra("EXTRA_EMAIL")

        // Устанавливаем полученные данные в TextView
        tvName.text = "Имя: $receivedName"
        tvEmail.text = "Email: $receivedEmail"

        // Обрабатываем нажатие кнопки "Назад"
        btnBack.setOnClickListener {
            // Просто завершаем текущую Activity, возвращаясь к MainActivity
            finish()
        }
    }
}