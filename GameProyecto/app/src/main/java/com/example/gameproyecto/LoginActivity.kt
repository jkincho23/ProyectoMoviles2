package com.example.gameproyecto

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.content.ContextCompat

class LoginActivity : AppCompatActivity() {
    private lateinit var imageViewPersonaje: ImageView
    private lateinit var et_nombre: EditText
    private lateinit var tv_bestScore: TextView
    private lateinit var playBtn: Button
    private lateinit var mp : MediaPlayer
    private lateinit var gameOption: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setIcon(R.mipmap.ic_launcher);

        mp = MediaPlayer.create(this,R.raw.alphabet_song)
        mp.start()
        mp.isLooping = true

        initComponents()


/*        val admin = AdminnSALiteOpenHelper(this,"DB", null,1)
        val db = admin.getWritableDatabase()

        val consulta = db.rawQuery(
            "select * from puntaje where score = (select max(score) from puntaje)", null
        )

        if(consulta.moveToFirst()){
            val temp_nombre = consulta.getString(0)
            val temp_score = consulta.getString(1)

            tv_bestScore.text = "Record: " + temp_score + " de " + temp_nombre
            db.close()
        }*/

        playBtn.setOnClickListener{jugar()}
    }

    fun jugar(){
        val nombre  = et_nombre.text.toString()

        if(!nombre.equals("")){
            mp.stop()
            mp.release()

            val intent:Intent

            if(gameOption == "fruits"){
                intent = Intent(this,MainActivityNivel1::class.java)
            }else{
                intent = Intent(this,MainActivityWords1::class.java)
            }
            intent.putExtra("Jugador",nombre)
            startActivity(intent)
            finish()


        }else{
            Toast.makeText(this, "Debe escribir su nombre", Toast.LENGTH_SHORT).show()
            et_nombre.requestFocus()
            //val imm =  getSystemService(this.INPUT_METHOD_SERVICE)
            //imm.showSofInput(et_nombre, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun initComponents(){
        imageViewPersonaje = findViewById(R.id.imageView_game)
        et_nombre = findViewById(R.id.et_nombre)
        playBtn = findViewById(R.id.playBtn)

        gameOption = intent.getStringExtra("game").toString()

        if(gameOption ==  "fruits"){
            imageViewPersonaje.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.manzana))
        }else{
            imageViewPersonaje.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.word_smiling))
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        mp.stop()
        mp.release()
        startActivity(intent)
        finish()
    }
}