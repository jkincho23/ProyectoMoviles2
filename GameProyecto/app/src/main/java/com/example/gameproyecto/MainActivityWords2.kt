package com.example.gameproyecto

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivityWords2 : AppCompatActivity() {
    private lateinit var rootDataBaseRef: DatabaseReference
    private lateinit var fireBaseAuth: FirebaseAuth

    private lateinit var checkBtn: Button
    private lateinit var result: EditText
    private lateinit var tv_word: TextView
    private lateinit var tv_nombre: TextView
    private lateinit var tv_score: TextView
    private lateinit var tv_vidas: TextView
    private lateinit var iv_vidas: ImageView

    private lateinit var nombre_Jugador: String

    private var indice: Int = 0
    private var score: Int = 0
    private var vidas: Int = 0

    private lateinit var mp: MediaPlayer
    private lateinit var mpGreat: MediaPlayer
    private lateinit var mpBad: MediaPlayer

    val words = arrayOf(
        "apple", "dance", "ocean", "smile", "clock", "music", "fairy", "tiger", "happy", "river",
        "dream", "peace", "panda", "sunny", "cloud", "magic", "beach", "heart", "sugar", "wings",
        "beard", "pizza", "worry", "flame", "beast", "angel", "brave", "beauty", "piano", "quiet"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_activity2_words2)

        initComponents()
        randomWord()
    }
    private fun initComponents() {

        fireBaseAuth = Firebase.auth
        rootDataBaseRef = FirebaseDatabase.getInstance().reference.child("WordsData")

        val myToolbar: Toolbar = findViewById(R.id.toolbarWordsNivel2)
        setSupportActionBar(myToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.mipmap.ic_launcher)

        checkBtn = findViewById(R.id.btnComrpobarK)
        result =  findViewById(R.id.et_resultadoWords)
        tv_word = findViewById(R.id.tv_word)
        tv_nombre = findViewById(R.id.tv_nombre)
        tv_score =  findViewById(R.id.tv_score)
        tv_vidas = findViewById(R.id.tv_intentos)
        iv_vidas = findViewById(R.id.iv_vidas)

        vidas = 3

        mp = MediaPlayer.create(this, R.raw.alphabet_song)
        mp.start()
        mp.isLooping = true

        mpGreat = MediaPlayer.create(this, R.raw.wonderful)
        mpBad = MediaPlayer.create(this, R.raw.bad)

        checkBtn.setOnClickListener { play() }

        nombre_Jugador = intent.getStringExtra("Jugador").toString()
        tv_nombre.text = "Nombre:  $nombre_Jugador"

        score = Integer.parseInt(intent.getStringExtra("score").toString())
        tv_score.text = "Score: $score"

        vidas = Integer.parseInt(intent.getStringExtra("vidas").toString())
    }

    fun play() {

        if (!result.text.toString().equals("")) {
            if (words[indice] == result.text.toString()) {
                mpGreat.start()
                score++
                tv_score.text = "Score: $score"
            } else {
                mpBad.start()
                vidas--
                println("Vidas : $vidas")
                when (vidas) {
                    3 -> {
                        iv_vidas.setImageResource(R.drawable.tresvidask)
                    }
                    2 -> {
                        iv_vidas.setImageResource(R.drawable.dosvidask)
                        Toast.makeText(this, "Quedan dos manzanas", Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        iv_vidas.setImageResource(R.drawable.unavidak)
                        Toast.makeText(this, "Queda una manzana", Toast.LENGTH_SHORT).show()
                    }
                    0 -> {
                        mp.stop()
                        mp.release()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            result.setText("")
            ingresarResultado()
            randomWord()
        }else{
            Toast.makeText(this, "¡Type a response!", Toast.LENGTH_SHORT).show()
        }
    }

    fun randomWord(){ //este metodo genera la posicion random y desordena la palabra a mostrar
        val wordNumber = (words.indices).random()
        val tempWord = words[wordNumber].toCharArray().toMutableList()

        tempWord.shuffle()
        indice = wordNumber
        tv_word.text = tempWord.joinToString("")
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
}