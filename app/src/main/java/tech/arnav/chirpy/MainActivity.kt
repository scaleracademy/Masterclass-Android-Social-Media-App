package tech.arnav.chirpy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tech.arnav.chirpy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        _binding.btnAdd.setOnClickListener {
            val num1 = _binding.etNum1.text.toString().toInt()
            val num2 = _binding.etNum2.text.toString().toInt()
            val sum = num1 + num2
            _binding.tvResult.text = sum.toString()
        }

    }
}