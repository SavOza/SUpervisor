package com.aozan.courseadvisor.mainactivity

import android.app.ActionBar.LayoutParams
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.aozan.courseadvisor.addfilteractivity.AddFilterActivity
import com.aozan.courseadvisor.databinding.FragmentChooseFilterDialogBinding
import com.aozan.courseadvisor.gradreqactivity.GradReqActivity

class ChooseFilterDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentChooseFilterDialogBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChooseFilterDialogBinding.inflate(LayoutInflater.from(context))

        binding.icAdd.setOnClickListener{
            val intent = Intent(activity, AddFilterActivity::class.java)
            startActivity(intent)
        }

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