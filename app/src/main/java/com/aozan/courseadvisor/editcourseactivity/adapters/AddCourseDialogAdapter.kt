package com.aozan.courseadvisor.editcourseactivity.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aozan.courseadvisor.databinding.ItemAddCourseBinding

class AddCourseDialogAdapter(private var coursesList: List<String>, private var itemClickCallback: ((course: String) -> Unit)): RecyclerView.Adapter<AddCourseDialogAdapter.AddCourseDialogViewHolder>() {
    private var filteredList = coursesList
    private var filterText = ""

    inner class AddCourseDialogViewHolder(private val binding: ItemAddCourseBinding) : RecyclerView.ViewHolder(binding.root) {
        var course = ""
        fun bind() {
            binding.itemCourseName.text = course
            binding.btnAdd.setOnClickListener {
                itemClickCallback(course)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddCourseDialogAdapter.AddCourseDialogViewHolder {
        val binding = ItemAddCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddCourseDialogViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: AddCourseDialogAdapter.AddCourseDialogViewHolder, position: Int) {
        holder.course = filteredList[position]
        holder.bind()
    }

    fun updateData(newList: List<String>) {
        coursesList = newList
        filter(filterText)
    }

    fun filter(text: String) {
        filterText = text
        filteredList = coursesList.filter { item -> item.lowercase().contains(filterText.lowercase()) }
        notifyDataSetChanged()
    }
}