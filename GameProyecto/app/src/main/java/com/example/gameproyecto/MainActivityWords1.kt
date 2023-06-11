package com.example.gameproyecto

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class MainActivityWords1 : AppCompatActivity() {

    private lateinit var checkBtn: Button
    private lateinit var result: EditText
    private lateinit var tv_word: TextView
    private lateinit var tv_nombre: TextView
    private lateinit var tv_score: TextView
    private lateinit var tv_vidas: TextView

    private lateinit var nombre_Jugador: String
    private lateinit var string_Vcore: String
    private lateinit var string_Vidas: String

    private var indice: Int = 0

    private lateinit var mp: MediaPlayer
    private lateinit var mpGreat: MediaPlayer
    private lateinit var mpBad: MediaPlayer

    val words = arrayOf("cat", "dog", "car", "sky", "run", "sun", "hat", "pen", "bird", "fish", "kite",
        "ball", "lamp", "desk", "book", "door", "cup", "fan", "top", "key", "bus", "leg", "mat", "bag"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_words1)

        initComponents()
        randomWord()
    }

    fun play(){

        val response = result.text.toString()

        if(!result.text.toString().equals("")){
            if(words[indice] == result.text.toString()){
                Toast.makeText(this, "¡Your right!", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "¡Your wrong!", Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(this, "¡Enter a response!", Toast.LENGTH_SHORT).show()
        }

    }

    fun randomWord(){ //este metodo genera la posicion random y desordena la palabra a mostrar
        val wordNumber = (words.indices).random()
        val tempWord = words[wordNumber].toCharArray().toMutableList()

        tempWord.shuffle()
        indice = wordNumber
        tv_word.text = tempWord.joinToString("")
    }

    private fun initComponents() {
        checkBtn = findViewById(R.id.btnComrpobarK)
        result =  findViewById(R.id.et_resultadoWords)
        tv_word = findViewById(R.id.tv_word)
        tv_nombre = findViewById(R.id.tv_nombre)
        tv_score =  findViewById(R.id.tv_score)
        tv_vidas = findViewById(R.id.tv_intentos)


        mp = MediaPlayer.create(this, R.raw.alphabet_song)
        mp.start()
        mp.isLooping = true

        mpGreat = MediaPlayer.create(this, R.raw.wonderful)
        mpBad = MediaPlayer.create(this, R.raw.bad)

        checkBtn.setOnClickListener { play() }

        nombre_Jugador = intent.getStringExtra("Jugador").toString()
        tv_nombre.text = "Nombre:  $nombre_Jugador"

    }
}