package com.example.gameproyecto

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var rootDataBaseRef: DatabaseReference
    private lateinit var fireBaseAuth: FirebaseAuth

    lateinit var fruitsBtn: ImageButton
    lateinit var wordsBtn: ImageButton

    private lateinit var mp: MediaPlayer
    private lateinit var mpGreat: MediaPlayer
    private lateinit var mpBad: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.mipmap.ic_launcher)

        initComponents()
    }

    private fun initComponents() {
        fruitsBtn = findViewById(R.id.fruitBtn)
        wordsBtn = findViewById(R.id.wordsBtn)
        mp = MediaPlayer.create(this, R.raw.alphabet_song)
        mp.start()
        mp.isLooping = true

        mpGreat = MediaPlayer.create(this, R.raw.wonderful)
        mpBad = MediaPlayer.create(this, R.raw.bad)

        fruitsBtn.setOnClickListener { openLoginActivityFruits() }
        wordsBtn.setOnClickListener { openLoginActivityWords() }
    }

    fun openLoginActivityFruits(){
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("game", "fruits")
        mp.stop()
        mp.release()
        startActivity(intent)
        finish()
    }

    fun openLoginActivityWords(){
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("game", "words")
        mp.stop()
        mp.release()
        startActivity(intent)
        finish()
    }

    fun scoreBoard(view : View){

// Crear una lista de puntajes de ejemplo (puedes reemplazarla con tus propios datos)
        var scores = listOf(
            "Jugador 1 - 100",
            "Jugador 2 - 150",
            "Jugador 3 - 75"
        )
        var scores1 = listOf(
            "Jugador 1 - 100",
            "Jugador 2 - 150",
            "Jugador 3 - 751"
        )


// Inflar el diseño del popup
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.popup_layout, null)


        obtenerDatosFirebase { datosOrdenados ->
            println(datosOrdenados)
            scores = datosOrdenados.map { jugador ->
                val nombreJugador = jugador.substringBefore(" -")
                val score = jugador.substringAfter(" - ")
                "$nombreJugador - $score"
            }.take(4)
            // Configurar el ListView y su adaptador
            val scoreboardListView = popupView.findViewById<ListView>(R.id.scoreboardListView)
            scoreboardListView.adapter = ArrayAdapter(this, R.layout.item_score, R.id.scoreTextView, scores)

        }

        obtenerDatosFirebaseWords { datosW ->
            scores1 = datosW.map { jugador ->
                val nombreJugador = jugador.substringBefore(" -")
                val score = jugador.substringAfter(" - ")
                "$nombreJugador - $score"
            }.take(4)
            val scoreboardListViewW = popupView.findViewById<ListView>(R.id.scoreboardListViewW)
            scoreboardListViewW.adapter = ArrayAdapter(this, R.layout.item_score, R.id.scoreTextView, scores1)

        }


        // Crear el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Configurar la animación y el fondo del popup
        popupWindow.animationStyle = R.style.PopupAnimation
        popupWindow.setBackgroundDrawable(ColorDrawable())

        // Mostrar el popup en el centro de la pantalla
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        // Manejar el clic del botón Cerrar
        val closeButton = popupView.findViewById<Button>(R.id.closeButton)
        closeButton.setOnClickListener {
            // Cerrar el popup
            popupWindow.dismiss()
        }
    }

    fun obtenerDatosFirebase(callback: (List<String>) -> Unit) {
        fireBaseAuth = Firebase.auth
        rootDataBaseRef = FirebaseDatabase.getInstance().reference.child("MyData")
        val datos = mutableListOf<String>()

        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val nombreJugador = childSnapshot.key ?: ""
                    val dato = childSnapshot.getValue(Any::class.java)
                    dato?.let {
                        val dataString = "$nombreJugador - $dato"
                        datos.add(dataString)
                    }
                }
                val datosOrdenados = datos.sortedByDescending { obtenerScore(it) }
                callback(datosOrdenados)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejo de errores
                callback(datos)
            }
        }

        rootDataBaseRef.addListenerForSingleValueEvent(listener)
    }

    fun obtenerDatosFirebaseWords(callback: (List<String>) -> Unit) {
        fireBaseAuth = Firebase.auth
        rootDataBaseRef = FirebaseDatabase.getInstance().reference.child("WordsData")
        val datos = mutableListOf<String>()

        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val nombreJugador = childSnapshot.key ?: ""
                    val dato = childSnapshot.getValue(Any::class.java)
                    println(dato)
                    dato?.let {
                        val dataString = "$nombreJugador - $dato"
                        datos.add(dataString)
                    }
                }
                val datosOrdenados = datos.sortedByDescending { obtenerScore(it) }
                callback(datosOrdenados)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejo de errores
                callback(datos)
            }
        }

        rootDataBaseRef.addListenerForSingleValueEvent(listener)
    }

    private fun obtenerScore(dataString: String): Int {
        val regex = Regex("score=(\\d+)")
        val matchResult = regex.find(dataString)
        return matchResult?.groupValues?.getOrNull(1)?.toIntOrNull() ?: 0
    }




}


