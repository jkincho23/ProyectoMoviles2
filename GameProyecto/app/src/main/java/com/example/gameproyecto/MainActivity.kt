package com.example.gameproyecto

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

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

}