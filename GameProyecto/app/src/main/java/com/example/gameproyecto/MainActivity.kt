package com.example.gameproyecto

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    private lateinit var imageViewPersonaje: ImageView
    private lateinit var editTextTextPersonName: EditText
    private lateinit var button: Button
    private lateinit var mp : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        getSupportActionBar()?.setIcon(R.mipmap.ic_launcher);




    }

    fun initComponents(){
        imageViewPersonaje = findViewById(R.id.imageView_Personaje)
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName)
        button = findViewById(R.id.button)
    }
}