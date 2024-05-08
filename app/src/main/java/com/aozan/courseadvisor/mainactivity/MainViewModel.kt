package com.aozan.courseadvisor.mainactivity

import android.util.Log
import androidx.lifecycle.ViewModel
import com.aozan.courseadvisor.misc.ScrapeService
import com.aozan.courseadvisor.model.Course
import com.aozan.courseadvisor.model.Lesson
import com.aozan.courseadvisor.model.Section
import kotlinx.coroutines.delay
import org.jsoup.nodes.Document

class MainViewModel: ViewModel() {

    val allCoursesList = mutableListOf<Course>()


    suspend fun executeService(date: String): List<Course> {
        allCoursesList.clear()

        val allCourseCodes = fetchCourseCodes(date)
        val splitCodes = allCourseCodes.chunked((allCourseCodes.size + 2) / 3)

        fetchCourseFromCodes(date, splitCodes[0])
        fetchCourseFromCodes(date, splitCodes[1])
        fetchCourseFromCodes(date, splitCodes[2])

        return allCoursesList
    }

    suspend fun fetchCourseFromCodes(date: String, courseCodes: List<String>){
        val theDocu = fetchAllCourses(date, courseCodes)

        var lastCourse: String = "0"

        val courseElements = theDocu.select("th.ddlabel a[href*=bwckschd.p_disp_detail_sched]")
        for (elem in courseElements) { // REPEATED PER SECTION
            val words = elem.text().split(" - ").toMutableList()
            if (words.size == 5) {
                words.removeAt(1)
            }
            val courseName = words[0]
            val crn = words[1]
            val courseCode = words[2]
            val sectionCode = words[3]

            val courseSplit = courseCode.split(" ")

            if (courseSplit[1].first().code >= 53) // 5XX or higher coded - thus, not undergrad. Ignore these.
                continue

            val theNewSection = Section(sectionCode, crn)

            val tableElements = courseElements.parents().parents().next()[0].select("table.datadisplaytable tr:has(td)")
            for (tableElem in tableElements) { //REPEATED PER LESSON
                val time = tableElem.select("td:nth-of-type(2)").text()
                val day = tableElem.select("td:nth-of-type(3)").text()
                val location = tableElem.select("td:nth-of-type(4)").text()

                val timeList = time.split(" - ")

                if (timeList.size == 2) {
                    val theNewLessson = Lesson(day, location, timeList[0], timeList[1])
                    theNewSection.addLesson(theNewLessson)
                }
                else {
                    val theNewLessson = Lesson(day, location, null, null)
                    theNewSection.addLesson(theNewLessson)
                }
            }

            // Add the section to a course
            if (courseCode.last().isDigit()) { //Last char is digit - this is a main section
                if (lastCourse == courseCode) { //Same code as last course, so add to existing one
                    allCoursesList.last().addMainSection(theNewSection)
                }
                else { //New code, this is a new course - create it
                    val theNewCourse = Course(courseName, courseCode)
                    theNewCourse.addMainSection(theNewSection)
                    allCoursesList.add(theNewCourse)
                    lastCourse = courseCode
                }
            }
            else if (courseCode.last() == 'R' || courseCode.last() == 'N'){ //'N' for CIP
                // This is a recit section - add to the prev course
                allCoursesList.last().addRecitSection(theNewSection)
            }
            else {
                // This is a lab section - add to lab sections
                allCoursesList.last().addLabSection(theNewSection)
            }

        }
    }

    // Get course codes - CS
    suspend fun fetchCourseCodes(date: String): List<String> {
        return ScrapeService.fetchCourseCodes(date)
    }

    // Get courses - CS201
    suspend fun fetchAllCourses(date: String, courses: List<String>): Document {
        return ScrapeService.fetchAllCourses(date, courses)
    }

}
