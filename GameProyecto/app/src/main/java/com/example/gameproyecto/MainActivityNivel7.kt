package com.example.gameproyecto

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.HashMap

class MainActivityNivel7 : AppCompatActivity() {
    private lateinit var rootDataBaseRef: DatabaseReference
    private lateinit var fireBaseAuth: FirebaseAuth

    private lateinit var myToolbar : Toolbar
    private lateinit var mp : MediaPlayer
    private lateinit var mpGreat : MediaPlayer
    private lateinit var mpBad : MediaPlayer
    private lateinit var tv_nombre : TextView
    private lateinit var tv_score : TextView
    private lateinit var et_Respuesta : EditText
    private lateinit var ivAuno : ImageView
    private lateinit var iv_Vidas : ImageView
    private lateinit var ivAdos : ImageView
    private lateinit var ivATres : ImageView

    private var score: Int = 0
    private var numAleatorio_Uno: Int = 0
    private var numAleatorio_Dos: Int = 0
    private var numAleatorio_Tres: Int = 0
    private var result: Int = 0
    private var vidas: Int = 0

    private lateinit var nombre_Jugador: String
    private lateinit var string_Vcore: String
    private lateinit var string_Vidas: String

    val numeros = arrayOf("cero", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_nivel7)

        Toast.makeText(this, "Nivel 7 - Combinadas", Toast.LENGTH_SHORT).show()
        initComponents()
        numeroAleatorio()

    }

    fun initComponents(){
        fireBaseAuth = Firebase.auth
        rootDataBaseRef = FirebaseDatabase.getInstance().reference.child("MyData")

        tv_score = findViewById(R.id.tv_score)
        tv_nombre = findViewById(R.id.tv_nombre)
        ivAuno = findViewById(R.id.NumeroUno)
        ivAdos = findViewById(R.id.NumeroDos)
        ivATres = findViewById(R.id.NumeroTres)
        iv_Vidas = findViewById(R.id.imageView)
        et_Respuesta = findViewById(R.id.et_resultado)
        vidas = 3
        nombre_Jugador = intent.getStringExtra("Jugador").toString()
        tv_nombre.text = "Jugador $nombre_Jugador"

        string_Vcore = intent.getStringExtra("score").toString()
        score = Integer.parseInt(string_Vcore)
        tv_score.text = "Score: $score"

        string_Vidas = intent.getStringExtra("vidas").toString()
        vidas = Integer.parseInt(string_Vidas)

        when (vidas) {
            3 -> iv_Vidas.setImageResource(R.drawable.tresvidas)
            2 -> iv_Vidas.setImageResource(R.drawable.dosvidas)
            1 -> iv_Vidas.setImageResource(R.drawable.unavida)
        }

        val myToolbar: Toolbar = findViewById(R.id.toolbarNivel7)
        setSupportActionBar(myToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.mipmap.ic_launcher)

        mp = MediaPlayer.create(this,R.raw.alphabet_song)
        mp.start()
        mp.isLooping = true

        mpGreat = MediaPlayer.create(this, R.raw.wonderful)
        mpBad = MediaPlayer.create(this, R.raw.bad)
    }

    fun comparar(view: View) {
        val respuesta = et_Respuesta.text.toString()

        if (respuesta.isNotEmpty()) {

            println("numAleatorio_Uno: $numAleatorio_Uno")
            println("numAleatorio_Dos: $numAleatorio_Dos")
            println("Comparison result: ${(numAleatorio_Uno + numAleatorio_Dos) == respuesta.toInt()}")

            if (result == respuesta.toInt()) {
                mpGreat.start()
                score++
                tv_score.text = "Score: $score"
            }
            else {
                mpBad.start()
                vidas--
                println("Vidas : ${vidas}")
                when (vidas) {
                    3 -> {
                        iv_Vidas.setImageResource(R.drawable.tresvidas)
                    }
                    2 -> {
                        iv_Vidas.setImageResource(R.drawable.dosvidas)
                        Toast.makeText(this, "Quedan dos manzanas", Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        iv_Vidas.setImageResource(R.drawable.unavida)
                        Toast.makeText(this, "Queda una manzana", Toast.LENGTH_SHORT).show()
                    }
                    0 -> {
                        iv_Vidas.setImageResource(R.drawable.dosvidas)
                        mp.stop()
                        mp.release()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            ingresarResultado()
            et_Respuesta.setText("")
            numeroAleatorio()
        } else {
            Toast.makeText(this, "Debes dar una respuesta", Toast.LENGTH_SHORT).show()
        }
    }



    fun numeroAleatorio() {

        if (score <= 13) {
            numAleatorio_Uno = (0..9).random()
            numAleatorio_Dos = (0..9).random()
            numAleatorio_Tres = (0..9).random()

            result = numAleatorio_Uno - numAleatorio_Dos + numAleatorio_Tres

            if (result >= 0) {
                for (i in 0 until numeros.size) {
                    val id = resources.getIdentifier(numeros[i], "drawable", packageName)
                    if (numAleatorio_Uno == i) {
                        ivAuno.setImageResource(id)
                    }
                    if (numAleatorio_Dos == i) {
                        ivAdos.setImageResource(id)
                    }

                    if (numAleatorio_Tres == i) {
                        ivATres.setImageResource(id)
                    }
                }
            }
            else
            {
                numeroAleatorio()
            }
        }
        else {
            val intent = Intent(this, MainActivityNivel8::class.java)
            string_Vcore = score.toString()
            string_Vidas = vidas.toString()


            intent.putExtra("Jugador", nombre_Jugador)
            intent.putExtra("score", string_Vcore)
            intent.putExtra("vidas", string_Vidas)



            mp.stop()
            mp.release()
            startActivity(intent)
            finish()

        }
    }

    fun ingresarResultado() {

        val datosJugador = HashMap<String, Any>()


        datosJugador["score"] = score
        val nuevoChildRef = rootDataBaseRef.child(nombre_Jugador)

        nuevoChildRef.setValue(datosJugador)
            .addOnSuccessListener {
                Toast.makeText(this, "Datos guardados en Firebase", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar los datos en Firebase", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Are you sure you want to exit?")
            .setCancelable(false)
            .setPositiveButton("Sí") { dialog, id ->
                val intent = Intent(this, MainActivity::class.java)
                mp.stop()
                mp.release()
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

}
