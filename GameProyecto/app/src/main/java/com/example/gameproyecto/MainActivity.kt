package com.example.gameproyecto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    lateinit var fruitsBtn: ImageButton
    lateinit var wordsBtn: ImageButton

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

        fruitsBtn.setOnClickListener { openLoginActivityFruits() }
        wordsBtn.setOnClickListener { openLoginActivityWords() }
    }

    fun openLoginActivityFruits(){
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("game", "fruits")
        startActivity(intent)
        finish()
    }

    fun openLoginActivityWords(){
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("game", "words")
        startActivity(intent)
        finish()
    }

}