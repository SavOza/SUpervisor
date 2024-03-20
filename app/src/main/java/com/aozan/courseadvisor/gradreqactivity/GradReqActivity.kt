package com.aozan.courseadvisor.gradreqactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aozan.courseadvisor.databinding.ActivityGradReqBinding
import com.aozan.courseadvisor.editcourseactivity.EditCourseActivity
import com.aozan.courseadvisor.mainactivity.ChooseFilterDialogFragment

class GradReqActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGradReqBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGradReqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClicks()
    }

    private fun setOnClicks() {
        binding.gradBackarrow.setOnClickListener {
            finish()
        }

        binding.btnEditprogram.setOnClickListener{
            val prev = supportFragmentManager.findFragmentByTag("editProgramDialog")
            if (prev == null){
                val dialog = EditProgramDialogFragment()
                dialog.show(supportFragmentManager, "editProgramDialog")
            }
        }

        binding.btnEditcourse.setOnClickListener {
            val intent = Intent(this, EditCourseActivity::class.java)
            startActivity(intent)
        }
    }
}