package com.aozan.courseadvisor.editcourseactivity.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aozan.courseadvisor.databinding.ItemEditCourseBinding

class EditCourseAdapter(private var coursesList: List<String>, private var itemClickCallback: ((course: String) -> Unit)) : RecyclerView.Adapter<EditCourseAdapter.EditCourseViewHolder>() {

    inner class EditCourseViewHolder(private val binding: ItemEditCourseBinding) : RecyclerView.ViewHolder(binding.root) {
        var course = ""
        fun bind() {
            binding.itemCourseName.text = course
            binding.btnRemove.setOnClickListener {
                itemClickCallback(course)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditCourseViewHolder {
        val binding = ItemEditCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EditCourseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return coursesList.size
    }

    override fun onBindViewHolder(holder: EditCourseViewHolder, position: Int) {
        holder.course = coursesList[position]
        holder.bind()
    }

    fun updateData(newList: List<String>) {
        coursesList = newList
        notifyDataSetChanged()
    }
}


