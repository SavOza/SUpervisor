package com.aozan.courseadvisor.editcourseactivity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aozan.courseadvisor.databinding.ActivityEditCourseBinding
import com.aozan.courseadvisor.editcourseactivity.adapters.EditCourseAdapter
import com.aozan.courseadvisor.gradreqactivity.GradReqViewModel
import com.aozan.courseadvisor.gradreqactivity.adapters.GradReqAdapter
import com.aozan.courseadvisor.model.Section

class EditCourseActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditCourseBinding
    private lateinit var viewModel: EditCourseViewModel
    private lateinit var adapter: EditCourseAdapter
    private lateinit var recView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[EditCourseViewModel::class.java]

        recView = binding.editCourseRecView
        recView.layoutManager = LinearLayoutManager(this)

        adapter = EditCourseAdapter(emptyList(), fun(course: String) {
            viewModel.removeTakenCourse(course)
        })

        recView.adapter = adapter

        viewModel.takenCourses.observe(this, Observer { items ->
            adapter.updateData(items)
        })


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

        binding.btnImportsched.setOnClickListener {
            val sharedPrefs = getSharedPreferences("courseAdvisorSharedPref", Context.MODE_PRIVATE)
            val scheduledSet = sharedPrefs.getStringSet("scheduledCourses", emptySet()) ?: emptySet()
            val scheduledList = scheduledSet.toList().sorted()

            for (item in scheduledList)
                viewModel.addTakenCourse(item)
        }
    }

    override fun onPause() {
        super.onPause()
        val sharedPrefs = getSharedPreferences("courseAdvisorSharedPref", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        val takenSet = viewModel.takenCourses.value?.toSet() ?: emptySet()

        editor.putStringSet("takenCourses", takenSet)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        val sharedPrefs = getSharedPreferences("courseAdvisorSharedPref", Context.MODE_PRIVATE)
        val programsSet = sharedPrefs.getStringSet("takenCourses", emptySet()) ?: emptySet()
        val theList = programsSet.toList().sorted()

        viewModel.initializeLists(theList)
    }
}