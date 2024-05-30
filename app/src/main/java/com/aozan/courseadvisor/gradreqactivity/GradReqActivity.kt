package com.aozan.courseadvisor.gradreqactivity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aozan.courseadvisor.databinding.ActivityGradReqBinding
import com.aozan.courseadvisor.editcourseactivity.EditCourseActivity
import com.aozan.courseadvisor.gradreqactivity.adapters.GradReqAdapter

class GradReqActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGradReqBinding
    private lateinit var adapter: GradReqAdapter
    private lateinit var recView: RecyclerView
    private lateinit var viewModel: GradReqViewModel

    val colorRed = Color.parseColor("#600505")
    val colorGreen = Color.parseColor("#0C6005")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGradReqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[GradReqViewModel::class.java]

        recView = binding.gradReqList
        recView.layoutManager = LinearLayoutManager(this)

        adapter = GradReqAdapter(emptyList())

        recView.adapter = adapter

        viewModel.programs.observe(this, Observer { items ->
            adapter.updateItems(items)
            setFulfillment(adapter.evaluateGraduation())
        })

        setOnClicks()


        val sharedPrefs = getSharedPreferences("courseAdvisorSharedPref", Context.MODE_PRIVATE)
        val programsSet = sharedPrefs.getStringSet("programs", emptySet()) ?: emptySet()
        val theList = programsSet.toList().sorted()

        for (item in theList) {
            viewModel.addProgram(item)
        }
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

        binding.checkBox.setOnClickListener {
            if (binding.checkBox.isChecked) {
                val sharedPrefs = getSharedPreferences("courseAdvisorSharedPref", Context.MODE_PRIVATE)
                val scheduledSet = sharedPrefs.getStringSet("scheduledCourses", emptySet()) ?: emptySet()
                val scheduledList = scheduledSet.toList()
                viewModel.enableSchedInclusion(scheduledList)
            }
            else {
                viewModel.disableSchedInclusion()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        val sharedPrefs = getSharedPreferences("courseAdvisorSharedPref", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val programsSet = mutableSetOf<String>()

        val programs = viewModel.programs.value
        if (!programs.isNullOrEmpty()) {
            for (program in programs) {
                programsSet.add(program.programName)
            }
        }

        editor.putStringSet("programs", programsSet)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        val sharedPrefs = getSharedPreferences("courseAdvisorSharedPref", Context.MODE_PRIVATE)
        val takenSet = sharedPrefs.getStringSet("takenCourses", emptySet()) ?: emptySet()
        val takenList = takenSet.toList().sorted()

        viewModel.setTakenCourse(takenList)
        setFulfillment(adapter.evaluateGraduation())
    }

    fun setFulfillment(isFulfilled: Boolean) {
        if (isFulfilled) {
            binding.linearlayoutRequirementbanner.background.setTint(colorGreen)
            binding.textviewRequirementinfo.text = "Requirements MET"
        }
        else {
            binding.linearlayoutRequirementbanner.background.setTint(colorRed)
            binding.textviewRequirementinfo.text = "Requirements NOT MET"
        }
    }
}