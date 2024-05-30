package com.aozan.courseadvisor.gradreqactivity

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aozan.courseadvisor.misc.DatabaseHelper
import com.aozan.courseadvisor.model.Program

class GradReqViewModel(application: Application) : AndroidViewModel(application) {

    private val _programs = MutableLiveData<MutableList<Program>>().apply { value = mutableListOf() }
    val programs: LiveData<MutableList<Program>>
        get() = _programs

    private var takenCourses = emptyList<String>()

    private val dbHelper = DatabaseHelper(application)
    private val db = dbHelper.readableDatabase

    private var scheduledCourses = emptyList<String>()
    private var schedIsIncluded = false


    fun addProgram(programName: String) {
        val reqCount = if (programName == "CS") 11 else 12
        val coreCount = if (programName == "CS") 31 else 29
        val theProgram = Program(programName,16, reqCount, coreCount, 9, 15, 5, 60, 90, 125, 240)
        _programs.value?.add(theProgram)
        calculateGradReq()
    }

    fun removeProgram(programName: String) {
        if (_programs.value != null) {
            if (_programs.value!![0].programName == programName) {
                _programs.value!!.removeAt(0)
            }
            else {
                _programs.value!!.removeAt(1)
            }

            _programs.value = _programs.value
        }
    }

    fun setTakenCourse(course: List<String>) {
        takenCourses = course
        calculateGradReq()
    }

    fun enableSchedInclusion(scheduledCourses: List<String>) {
        schedIsIncluded = true
        this.scheduledCourses = scheduledCourses
        calculateGradReq()
    }

    fun disableSchedInclusion() {
        schedIsIncluded = false
        calculateGradReq()
    }

    fun calculateGradReq() {
        if (_programs.value.isNullOrEmpty())
            return

        for (program in _programs.value!!) {
            program.resetTaken()
        }

        val theFullCourseList: MutableList<String> = takenCourses.toMutableList()
        if (schedIsIncluded) {
            for (item in scheduledCourses) {
                if (!theFullCourseList.contains(item))
                    theFullCourseList.add(item)
            }
        }

        for (course in theFullCourseList) {
            val cursor = db.query("Courses", null, "courseCode = ?",
                arrayOf(course), null, null, null)

            if (cursor.moveToFirst()) {
                val ectsCreds = cursor.getInt(cursor.getColumnIndexOrThrow("ectsCreds"))
                val suCred = cursor.getInt(cursor.getColumnIndexOrThrow("suCred"))
                val csType = cursor.getInt(cursor.getColumnIndexOrThrow("csType"))
                val ieType = cursor.getInt(cursor.getColumnIndexOrThrow("ieType"))
                val scienceCred = cursor.getInt(cursor.getColumnIndexOrThrow("scienceCred"))
                val engineeringCred = cursor.getInt(cursor.getColumnIndexOrThrow("engineeringCred"))
                val faculty = cursor.getString(cursor.getColumnIndexOrThrow("faculty"))

                for (program in _programs.value!!) {
                    program.ectsTaken += ectsCreds
                    program.suTaken += suCred
                    program.sciTaken += scienceCred
                    program.engTaken += engineeringCred
                    if (faculty != null)
                        program.facTaken += 1

                    if (program.programName == "CS") {
                        when (csType) {
                            0 -> program.ucTaken += 1
                            1 -> program.rcTaken += 1
                            2 -> {
                                if (program.ceTaken < program.ceReq)
                                    program.ceTaken += suCred
                                else if (program.aeTaken < program.aeReq)
                                    program.aeTaken += suCred
                                else
                                    program.feTaken += suCred
                            }
                            3 -> {
                                if (program.aeTaken < program.aeReq)
                                    program.aeTaken += suCred
                                else
                                    program.feTaken += suCred
                            }
                            4 -> program.feTaken += suCred
                        }
                    }
                    else {
                        when (ieType) {
                            0 -> program.ucTaken += 1
                            1 -> program.rcTaken += 1
                            2 -> {
                                if (program.ceTaken < program.ceReq)
                                    program.ceTaken += suCred
                                else if (program.aeTaken < program.aeReq)
                                    program.aeTaken += suCred
                                else
                                    program.feTaken += suCred
                            }
                            3 -> {
                                if (program.aeTaken < program.aeReq)
                                    program.aeTaken += suCred
                                else
                                    program.feTaken += suCred
                            }
                            4 -> program.feTaken += suCred
                        }
                    }
                }




            }
            cursor.close()
        }

        _programs.value!!.sortBy { it.programName }

        _programs.value = _programs.value
    }
}