package com.aozan.courseadvisor.editcourseactivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aozan.courseadvisor.databinding.FragmentAddCourseDialogBinding
import com.aozan.courseadvisor.editcourseactivity.adapters.AddCourseDialogAdapter
import com.aozan.courseadvisor.editcourseactivity.adapters.EditCourseAdapter
import com.aozan.courseadvisor.gradreqactivity.GradReqViewModel

class AddCourseDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentAddCourseDialogBinding
    private lateinit var viewModel: EditCourseViewModel
    private lateinit var adapter: AddCourseDialogAdapter
    private lateinit var recView: RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddCourseDialogBinding.inflate(LayoutInflater.from(context))
        viewModel = ViewModelProvider(requireActivity())[EditCourseViewModel::class.java]

        recView = binding.addCourseList
        recView.layoutManager = LinearLayoutManager(requireContext())

        adapter = AddCourseDialogAdapter(emptyList(), fun(course: String) {
            viewModel.addTakenCourse(course)
        })
        recView.adapter = adapter

        viewModel.remainingCourses.observe(this, Observer { items ->
            adapter.updateData(items)
        })

        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        var params = dialog?.window?.attributes

        if (params != null) {
            params.width = 1000
            params.height = 1200
        }

        dialog?.window?.attributes = params
    }
}