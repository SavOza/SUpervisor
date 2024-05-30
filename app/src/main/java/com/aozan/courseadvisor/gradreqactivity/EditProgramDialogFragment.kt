package com.aozan.courseadvisor.gradreqactivity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.aozan.courseadvisor.databinding.FragmentEditProgramDialogBinding

class EditProgramDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentEditProgramDialogBinding
    private lateinit var viewModel: GradReqViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditProgramDialogBinding.inflate(LayoutInflater.from(context))
        viewModel = ViewModelProvider(requireActivity())[GradReqViewModel::class.java]

        setOnClicks()
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

        val programs = viewModel.programs.value
        if (!programs.isNullOrEmpty()) {
            for (program in programs) {
                if (program.programName == "CS")
                    binding.btnCS.isChecked = true
                else
                    binding.btnIE.isChecked = true
            }
        }
    }

    fun setOnClicks() {
        binding.btnCS.setOnClickListener {
            if(binding.btnCS.isChecked) {
                viewModel.addProgram("CS")
            }
            else {
                viewModel.removeProgram("CS")
            }
        }

        binding.btnIE.setOnClickListener {
            if(binding.btnIE.isChecked) {
                viewModel.addProgram("IE")
            }
            else {
                viewModel.removeProgram("IE")
            }
        }
    }
}