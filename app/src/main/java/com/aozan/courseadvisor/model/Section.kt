package com.aozan.courseadvisor.model

class Section (val sectionCode: String, val crn: String, var isToggled: Boolean = false) {
    private val _lessons = mutableListOf<Lesson>()
    var courseCode: String = ""

    var lecturer: String? = null
        set(value) {
            field = value
        }

    val lesson: List<Lesson>
        get() = _lessons

    fun addLesson(lesson: Lesson) {
        _lessons.add(lesson)
    }
}