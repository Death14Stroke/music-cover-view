package com.andruid.magic.coversample

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.andruid.magic.coversample.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playBtn.setOnClickListener {
            binding.cover.isPlaying = !binding.cover.isPlaying
        }

        lifecycleScope.launch {
            delay(5000)
            binding.cover.strokeColor = Color.GREEN
            delay(3000)
            binding.cover.ringColor = Color.YELLOW
        }
    }
}