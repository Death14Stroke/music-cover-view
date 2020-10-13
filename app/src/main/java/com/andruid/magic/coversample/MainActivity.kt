package com.andruid.magic.coversample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.andruid.magic.coversample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playBtn.setOnClickListener {
            Log.d("animLog", "isPlaying = ${binding.cover.isPlaying()}")
            if (binding.cover.isPlaying())
                binding.cover.pause()
            else
                binding.cover.play()
        }
    }
}