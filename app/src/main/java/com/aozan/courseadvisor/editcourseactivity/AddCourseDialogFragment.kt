package com.aozan.courseadvisor.editcourseactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.aozan.courseadvisor.databinding.FragmentAddCourseDialogBinding

class AddCourseDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentAddCourseDialogBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddCourseDialogBinding.inflate(LayoutInflater.from(context))

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