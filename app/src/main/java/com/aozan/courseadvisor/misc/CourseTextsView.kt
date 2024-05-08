package com.aozan.courseadvisor.misc

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.aozan.courseadvisor.R
class CourseTextsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private val textCourse: TextView
    private val textClass: TextView
    private val courseBackground: LinearLayout

    init {
        inflate(context, R.layout.course_texts_view, this)
        textCourse = findViewById(R.id.courseTextView)
        textClass = findViewById(R.id.classTextView)
        courseBackground = findViewById(R.id.courseBackground)

        attrs?.let { setAttributes(context, it) }
    }

    private fun setAttributes(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CourseTextsView)
        val courseText = typedArray.getString(R.styleable.CourseTextsView_courseText)
        val classText = typedArray.getString(R.styleable.CourseTextsView_classText)
        val bgColor = typedArray.getColor(R.styleable.CourseTextsView_bgColor, Color.RED)

        try {
            this.textCourse.text = courseText
            this.textClass.text = classText
            this.courseBackground.background.setTint(bgColor)
        }
        finally {
            typedArray.recycle()
        }

    }

    fun setCourseName(str: String) {
        this.textCourse.text = str
    }

    fun setClassName(str: String) {
        this.textClass.text = str
    }

    fun setBackground(str: Int) {
        this.courseBackground.background.setTint(str)
    }

}