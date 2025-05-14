package com.deportes.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ConsultasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultas)


        val btnListarSocios = findViewById<Button>(R.id.btnListarSocios)
        val btnCarnet = findViewById<Button>(R.id.btnCarnet)
        val btnPrecios = findViewById<Button>(R.id.btnPrecios)

        btnListarSocios.setOnClickListener {
        }

        btnCarnet.setOnClickListener {
            val intent = Intent(this, CarnetActivity::class.java)
            startActivity(intent)}

        btnPrecios.setOnClickListener {
        val intent = Intent(this, PreciosActivity::class.java)
        startActivity(intent)}
    }
}
