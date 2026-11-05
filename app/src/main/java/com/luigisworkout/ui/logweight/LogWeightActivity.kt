package com.luigisworkout.ui.logweight

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.luigisworkout.R
import com.luigisworkout.databinding.ActivityLogWeightBinding
import com.luigisworkout.luigiApp
import com.luigisworkout.viewmodel.LogWeightViewModel
import com.luigisworkout.viewmodel.appViewModelFactory

class LogWeightActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogWeightBinding
    private val viewModel: LogWeightViewModel by viewModels {
        appViewModelFactory(luigiApp)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogWeightBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.log_weight)

        binding.btnSaveWeight.setOnClickListener {
            val weightStr = binding.etWeight.text.toString()
            val weight = weightStr.trim().replace(',', '.').toFloatOrNull()

            if (weight == null || weight !in 30f..300f) {
                binding.etWeight.error = getString(R.string.weight_invalid)
                return@setOnClickListener
            }

            val note = binding.etWeightNote.text.toString().trim()
            viewModel.saveWeight(weight, note)
        }

        viewModel.saved.observe(this) { saved ->
            if (saved) {
                Toast.makeText(this, R.string.weight_saved, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
