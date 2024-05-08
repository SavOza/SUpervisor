package com.aozan.courseadvisor.model

data class Section (val sectionCode: String, val crn: String) {
    private val _lessons = mutableListOf<Lesson>()

    val lesson: List<Lesson>
        get() = _lessons

    fun addLesson(lesson: Lesson) {
        _lessons.add(lesson)
    }
}