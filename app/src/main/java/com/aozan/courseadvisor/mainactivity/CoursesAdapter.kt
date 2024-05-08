package com.aozan.courseadvisor.mainactivity

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aozan.courseadvisor.databinding.CoursesItemBinding
import com.aozan.courseadvisor.model.Course

class CoursesAdapter(private var dataList: List<Course>) : RecyclerView.Adapter<CoursesAdapter.CoursesViewHolder>() {

    class CoursesViewHolder(private val binding: CoursesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var course: Course? = null

        fun setView() {
            if (course == null)
                return

            val courseCode = course!!.courseCode
            val courseName = course!!.courseName
            val fullText = "$courseCode - $courseName"

            binding.recItemCourseName.text = fullText
            binding.recItemProfName.text = course!!.mainSection[0].crn
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursesViewHolder {
        val binding = CoursesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoursesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoursesViewHolder, position: Int) {
        Log.d("IN VIEWHOLDER", dataList[position].courseCode)
        holder.course = dataList[position]
        holder.setView()
    }

    fun updateData(dataList: List<Course>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }
    override fun getItemCount() = dataList.size
}