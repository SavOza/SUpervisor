package com.aozan.courseadvisor.gradreqactivity.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aozan.courseadvisor.databinding.ItemGradreqBinding
import com.aozan.courseadvisor.model.Program

class GradReqAdapter(private var programsList: List<Program>) : RecyclerView.Adapter<GradReqAdapter.GradReqViewHolder>() {

    inner class GradReqViewHolder(private val binding: ItemGradreqBinding) : RecyclerView.ViewHolder(binding.root) {
        val green = Color.parseColor("#0C6005")
        val white = Color.WHITE
        fun bind(program: Program) {
            val programName = program.programName
            binding.programName.text = "MAJOR - $programName"

            // University Courses
            binding.ucReq.text = program.ucReq.toString()
            binding.ucTaken.text = program.ucTaken.toString()
            if (program.ucTaken >= program.ucReq)
                binding.uc.setTextColor(green)
            else {
                binding.uc.setTextColor(white)

            }
            // Required Courses
            binding.rcReq.text = program.rcReq.toString()
            binding.rcTaken.text = program.rcTaken.toString()
            if (program.rcTaken >= program.rcReq)
                binding.rc.setTextColor(green)
            else
                binding.rc.setTextColor(white)

            // Core Electives
            binding.ceReq.text = program.ceReq.toString()
            binding.ceTaken.text = program.ceTaken.toString()
            if (program.ceTaken >= program.ceReq)
                binding.ce.setTextColor(green)
            else
                binding.ce.setTextColor(white)

            // Area Electives
            binding.aeReq.text = program.aeReq.toString()
            binding.aeTaken.text = program.aeTaken.toString()
            if (program.aeTaken >= program.aeReq)
                binding.ae.setTextColor(green)
            else
                binding.ae.setTextColor(white)

            // Free Electives
            binding.feReq.text = program.feReq.toString()
            binding.feTaken.text = program.feTaken.toString()
            if (program.feTaken >= program.feReq)
                binding.fe.setTextColor(green)
            else
                binding.fe.setTextColor(white)

            // Faculty Courses
            binding.facReq.text = program.facReq.toString()
            binding.facTaken.text = program.facTaken.toString()
            if (program.facTaken >= program.facReq)
                binding.fac.setTextColor(green)
            else
                binding.fac.setTextColor(white)

            // Science Credit
            binding.sciReq.text = program.sciReq.toString()
            binding.sciTaken.text = program.sciTaken.toString()
            if (program.sciTaken >= program.sciReq)
                binding.sci.setTextColor(green)
            else
                binding.sci.setTextColor(white)

            // Engineering Credit
            binding.engReq.text = program.engReq.toString()
            binding.engTaken.text = program.engTaken.toString()
            if (program.engTaken >= program.engReq)
                binding.eng.setTextColor(green)
            else
                binding.eng.setTextColor(white)

            // SU Credits
            binding.suReq.text = program.suReq.toString()
            binding.suTaken.text = program.suTaken.toString()
            if (program.suTaken >= program.suReq)
                binding.su.setTextColor(green)
            else
                binding.su.setTextColor(white)

            // ECTS Credits
            binding.ectsReq.text = program.ectsReq.toString()
            binding.ectsTaken.text = program.ectsTaken.toString()
            if (program.ectsTaken >= program.ectsReq)
                binding.ects.setTextColor(green)
            else
                binding.ects.setTextColor(white)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradReqViewHolder {
        val binding = ItemGradreqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GradReqViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return programsList.size
    }

    override fun onBindViewHolder(holder: GradReqViewHolder, position: Int) {
        holder.bind(programsList[position])
    }

    fun updateItems(newItems: List<Program>) {
        programsList = newItems
        notifyDataSetChanged()
    }

    fun evaluateGraduation(): Boolean {
        for (item in programsList) {
            if (item.ucReq > item.ucTaken)
                return false

            if (item.rcReq > item.rcTaken)
                return false

            if (item.ceReq > item.ceTaken)
                return false

            if (item.aeReq > item.aeTaken)
                return false

            if (item.feReq > item.feTaken)
                return false

            if (item.facReq > item.facTaken)
                return false

            if (item.sciReq > item.sciTaken)
                return false

            if (item.engReq > item.engTaken)
                return false

            if (item.suReq > item.suTaken)
                return false

            if (item.ectsReq > item.ectsTaken)
                return false
        }
        return true
    }
}