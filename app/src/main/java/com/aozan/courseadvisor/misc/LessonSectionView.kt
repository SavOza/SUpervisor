package com.aozan.courseadvisor.misc

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aozan.courseadvisor.R

class LessonSectionView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private val sectionTitle: TextView
    private val lessonRecyclerView: RecyclerView

    init {
        inflate(context, R.layout.view_sections_list, this)
        sectionTitle = findViewById(R.id.sectionTitle)
        lessonRecyclerView = findViewById(R.id.lessonRecyclerView)
        lessonRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    fun setSectionTitle(title: String) {
        sectionTitle.text = title
    }
}
