package com.aozan.courseadvisor.mainactivity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import com.aozan.courseadvisor.databinding.ActivityMainBinding
import com.aozan.courseadvisor.gradreqactivity.GradReqActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setOnClicks()
    }


    private fun setOnClicks() {
        binding.icMenu.setOnClickListener {
                binding.mainDrawerLayout.openDrawer(GravityCompat.START)
        }

        binding.icGrad.setOnClickListener {
            val intent = Intent(this, GradReqActivity::class.java)
            startActivity(intent)
        }

        binding.sidebar.btnAdvancedfilter.setOnClickListener {
            val prev = supportFragmentManager.findFragmentByTag("chooseFilterDialog")
            if (prev == null){
                val dialog = ChooseFilterDialogFragment()
                dialog.show(supportFragmentManager, "chooseFilterDialog")
            }
        }
    }
}