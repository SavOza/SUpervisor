package com.aozan.courseadvisor.mainactivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.aozan.courseadvisor.addfilteractivity.AddFilterActivity
import com.aozan.courseadvisor.databinding.FragmentChooseFilterDialogBinding

class ChooseFilterDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentChooseFilterDialogBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChooseFilterDialogBinding.inflate(LayoutInflater.from(context))

        binding.icAdd.setOnClickListener{
            val intent = Intent(activity, AddFilterActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val params = dialog?.window?.attributes

        if (params != null) {
            params.width = 1000
            params.height = 1200
        }

        dialog?.window?.attributes = params
    }
}