package com.example.bettertimetoseeyou

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_second)

        val tvInfo = findViewById<TextView>(R.id.tvInfo)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        val p1 = MainActivity.TEXTO1;
        val p2 = MainActivity.NUMERO1;

        val textoRecibido = intent.getStringExtra(MainActivity.TEXTO1) ?: ""
        val numeroRecibido = intent.getIntExtra("NUMERO1", Int.MIN_VALUE)


        val resumen = buildString {
            appendLine("Info recibida de la Main Activity")
            appendLine("---------------------------------")
            appendLine("texto recibido: $textoRecibido"  )
            if (numeroRecibido != Int.MIN_VALUE){
                appendLine("Número recibido: $numeroRecibido")
            } else {
                appendLine("No se ha recibido ningún número")
            }
        }

        tvInfo.text = resumen;

        btnVolver.setOnClickListener { finish() }

    }

}