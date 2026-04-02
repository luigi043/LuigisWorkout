package com.luigisworkout.ui.logweight

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.luigisworkout.databinding.ActivityLogWeightBinding
import com.luigisworkout.viewmodel.AppViewModelFactory
import com.luigisworkout.viewmodel.LogWeightViewModel

class LogWeightActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogWeightBinding
    private val viewModel: LogWeightViewModel by viewModels {
        AppViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogWeightBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Registar Peso"

        binding.btnSaveWeight.setOnClickListener {
            val weightStr = binding.etWeight.text.toString()
            val weight = weightStr.toFloatOrNull()

            if (weight == null || weight < 30f || weight > 300f) {
                binding.etWeight.error = "Insere um peso válido (30-300 kg)"
                return@setOnClickListener
            }

            val note = binding.etWeightNote.text.toString().trim()
            viewModel.saveWeight(weight, note)
        }

        viewModel.saved.observe(this) { saved ->
            if (saved) {
                Toast.makeText(this, "✅ Peso guardado!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}