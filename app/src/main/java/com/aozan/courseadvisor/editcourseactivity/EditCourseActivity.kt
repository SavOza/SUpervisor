package com.aozan.courseadvisor.editcourseactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aozan.courseadvisor.R
import com.aozan.courseadvisor.databinding.ActivityEditCourseBinding
import com.aozan.courseadvisor.databinding.ActivityGradReqBinding
import com.aozan.courseadvisor.gradreqactivity.EditProgramDialogFragment

class EditCourseActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditCourseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClicks()
    }

    private fun setOnClicks() {
        binding.btnAddcourse.setOnClickListener{
            val prev = supportFragmentManager.findFragmentByTag("addCourseDialog")
            if (prev == null){
                val dialog = AddCourseDialogFragment()
                dialog.show(supportFragmentManager, "addCourseDialog")
            }
        }

        binding.editcourseBackarrow.setOnClickListener{
            finish()
        }

    }
}