package com.example.ladder_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ladder_test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.nxt.setOnClickListener {
            val intent = Intent(this, LadderActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }
}