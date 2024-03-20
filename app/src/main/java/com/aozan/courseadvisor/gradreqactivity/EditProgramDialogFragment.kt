package com.aozan.courseadvisor.gradreqactivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.aozan.courseadvisor.addfilteractivity.AddFilterActivity
import com.aozan.courseadvisor.databinding.FragmentChooseFilterDialogBinding
import com.aozan.courseadvisor.databinding.FragmentEditProgramDialogBinding

class EditProgramDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentEditProgramDialogBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditProgramDialogBinding.inflate(LayoutInflater.from(context))


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