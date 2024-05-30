package com.aozan.courseadvisor.mainactivity.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aozan.courseadvisor.databinding.ItemCourseBinding
import com.aozan.courseadvisor.databinding.ItemCourseExpandedBinding
import com.aozan.courseadvisor.databinding.ViewSectionsListBinding
import com.aozan.courseadvisor.model.Course
import com.aozan.courseadvisor.model.Section

class CoursesAdapter(private var dataList: List<Course>, private var itemClickCallback: ((section: Section, isToggled: Boolean) -> Unit)) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var expandedPosition: Int = -1
    private var filteredList = dataList
    private var filterText = ""

    inner class CoursesViewHolder(private val binding: ItemCourseBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setView(course: Course) {
            val courseCode = course.courseCode
            val courseName = course.courseName
            val fullText = "$courseCode - $courseName"

            binding.recItemCourseName.text = fullText
            binding.recItemProfName.text = course.mainSection[0].crn

            itemView.setOnClickListener {
                expandedPosition = adapterPosition
                notifyDataSetChanged()
            }
        }
    }

    inner class CourseExpandedViewHolder(private val binding: ItemCourseExpandedBinding) : RecyclerView.ViewHolder(binding.root) {
        private val sectionContainer: LinearLayout = binding.layoutSections

        fun setView(course: Course) {
            val courseCode = course.courseCode
            val courseName = course.courseName
            val fullText = "$courseCode - $courseName"

            binding.recItemCourseName.text = fullText
            binding.layoutSections.removeAllViews()

            if (course.mainSection.isNotEmpty()) {
                addSectionViews("Main Section", course.mainSection)
            }
            if (course.recitSections.isNotEmpty()) {
                addSectionViews("R Section", course.recitSections)
            }
            if (course.labSections.isNotEmpty()) {
                addSectionViews("L Section", course.labSections)
            }

            itemView.setOnClickListener {
                expandedPosition = -1
                notifyDataSetChanged()
            }
        }

        private fun addSectionViews(title: String, sections: List<Section>) {
            val sectionBinding = ViewSectionsListBinding.inflate(LayoutInflater.from(binding.root.context), binding.layoutSections, false)

            sectionBinding.sectionTitle.text = title
            sectionBinding.lessonRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
            sectionBinding.lessonRecyclerView.setHasFixedSize(true)
            sectionBinding.lessonRecyclerView.adapter = SectionAdapter(sections, itemClickCallback)
            binding.layoutSections.addView(sectionBinding.root)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == expandedPosition) 1 else 0 // 1 = Expanded, 0 = Normal
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) { //Normal
            val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CoursesViewHolder(binding)
        }
        else {
            val binding = ItemCourseExpandedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CourseExpandedViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val course = filteredList[position]
        if (holder is CoursesViewHolder) {
            holder.setView(course)
        }
        else if (holder is CourseExpandedViewHolder) {
            holder.setView(course)
        }
    }

    fun updateData(dataList: List<Course>) {
        this.dataList = dataList
        filter(filterText)
    }
    override fun getItemCount() = filteredList.size

    fun filter(text: String) {
        filterText = text
        filteredList = dataList.filter { item -> item.courseCode.lowercase().contains(filterText.lowercase()) }
        notifyDataSetChanged()
    }
}