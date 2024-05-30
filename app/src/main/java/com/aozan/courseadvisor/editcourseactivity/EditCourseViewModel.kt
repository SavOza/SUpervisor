package com.aozan.courseadvisor.editcourseactivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aozan.courseadvisor.misc.DatabaseHelper
import com.aozan.courseadvisor.model.Program

class EditCourseViewModel(application: Application) : AndroidViewModel(application) {
    private val _takenCourses = MutableLiveData<MutableList<String>>().apply { value = mutableListOf() }
    val takenCourses: LiveData<MutableList<String>>
        get() = _takenCourses

    private val _remainingCourses = MutableLiveData<MutableList<String>>().apply { value = mutableListOf() }
    val remainingCourses: LiveData<MutableList<String>>
        get() = _remainingCourses

    private val dbHelper = DatabaseHelper(application)
    private val db = dbHelper.readableDatabase



    fun initializeLists(takenCourse: List<String>) {
        _takenCourses.value?.addAll(takenCourse)

        val cursor = db.query("Courses", arrayOf("courseCode"), null,
            null, null, null, "courseCode ASC")

        with(cursor) {
            while(moveToNext()) {
                val courseCode = getString(getColumnIndexOrThrow("courseCode"))
                if (!takenCourse.contains(courseCode)) {
                    _remainingCourses.value?.add(courseCode)
                }
            }
        }

        cursor.close()
    }

    fun removeTakenCourse(takenCourse: String) {
        _takenCourses.value?.remove(takenCourse)
        if (_remainingCourses.value == null)
            return

        val index = _remainingCourses.value!!.binarySearch(takenCourse)
        val insertionPoint = if (index < 0) -(index + 1) else index
        _remainingCourses.value!!.add(insertionPoint, takenCourse)

        _remainingCourses.value = _remainingCourses.value
        _takenCourses.value = _takenCourses.value
    }

    fun addTakenCourse(takenCourse: String) {
        if (_takenCourses.value == null)
            return
        if (_takenCourses.value!!.contains(takenCourse))
            return

        _remainingCourses.value?.remove(takenCourse)


        val index = _takenCourses.value!!.binarySearch(takenCourse)
        val insertionPoint = if (index < 0) -(index + 1) else index
        _takenCourses.value!!.add(insertionPoint, takenCourse)

        _remainingCourses.value = _remainingCourses.value
        _takenCourses.value = _takenCourses.value
    }
}