package com.aozan.courseadvisor.addfilteractivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aozan.courseadvisor.R
import com.aozan.courseadvisor.databinding.ActivityAddFilterBinding
import com.aozan.courseadvisor.databinding.ActivityGradReqBinding

class AddFilterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.filterBackarrow.setOnClickListener {
            finish()
        }
    }
}