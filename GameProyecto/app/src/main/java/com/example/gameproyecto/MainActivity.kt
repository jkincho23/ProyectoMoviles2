package com.example.gameproyecto

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var imageViewPersonaje: ImageView
    private lateinit var et_nombre: EditText
    private lateinit var tv_bestScore: TextView
    private lateinit var playBtn: Button
    private lateinit var mp : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setIcon(R.mipmap.ic_launcher);

        mp = MediaPlayer.create(this,R.raw.alphabet_song)
        mp.start()
        mp.isLooping = true

        initComponents()


        val admin = AdminnSALiteOpenHelper(this,"DB", null,1)
        val db = admin.getWritableDatabase()

        val consulta = db.rawQuery(
                "select * from puntaje where score = (select max(score) from puntaje)", null
        )

        if(consulta.moveToFirst()){
            val temp_nombre = consulta.getString(0)
            val temp_score = consulta.getString(1)

            tv_bestScore.text = "Record: " + temp_score + " de " + temp_nombre
            db.close()
        }

        playBtn.setOnClickListener{jugar()}
    }

    fun jugar(){
        val nombre  = et_nombre.text.toString()

        if(!nombre.equals("")){
            mp.stop()
            mp.release()
            val intent = Intent(this,MainActivity2::class.java)
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

    @Override
    override fun onBackPressed(){

    }

    fun initComponents(){
        imageViewPersonaje = findViewById(R.id.imageView_Personaje)
        et_nombre = findViewById(R.id.editTextTextPersonName)
        playBtn = findViewById(R.id.button)
    }
}