package com.example.zadanie2004

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnBackToHome = findViewById<Button>(R.id.btnBackToHome)

        // Обработчик кнопки "Назад"
        btnBackToHome.setOnClickListener {
            finish() // Закрываем текущую активность и возвращаемся на HomeActivity
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("EXTRA_NAME", name)
                intent.putExtra("EXTRA_EMAIL", email)
                startActivity(intent)
            } else {
                android.widget.Toast.makeText(this, "Заполните все поля", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
}