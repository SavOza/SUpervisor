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

    var allCoursesList = listOf<Course>()

    val dayMap: Map<String, String> = mapOf( //To convert to actual date
        "M" to "MON",
        "T" to "TUE",
        "W" to "WED",
        "R" to "THU",
        "F" to "FRI"
    )



    suspend fun executeService(date: String): List<Course> {
        val allCourseCodes = fetchCourseCodes(date)
        val splitCodes = allCourseCodes.chunked((allCourseCodes.size + 2) / 3)

        val theListOne = fetchCourseFromCodes(date, splitCodes[0])
        val theListTwo = fetchCourseFromCodes(date, splitCodes[1])
        val theListThree = fetchCourseFromCodes(date, splitCodes[2])

        allCoursesList = theListOne + theListTwo + theListThree
        return allCoursesList
    }

    suspend fun fetchCourseFromCodes(date: String, courseCodes: List<String>): MutableList<Course> {
        val courseNewList = mutableListOf<Course>()

        val theDocu = fetchAllCourses(date, courseCodes)

        var lastCourse: String = "0"

        val courseElements = theDocu.select("th.ddlabel a[href*=bwckschd.p_disp_detail_sched]")
        var index = 0

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

            if (courseSplit[1].first().code >= 53) { // 5XX or higher coded - thus, not undergrad. Ignore these. Make sure to increment index
                index += 1
                continue
            }


            val theNewSection = Section(sectionCode, crn)

            var tableElements = courseElements.parents().parents().next()[index].select("table.datadisplaytable tr:has(td)")
            while(tableElements.isEmpty()) { //Due to some errors, some may have empty elements that need to be skipped over. Increase index and try the next one
                index += 1
                tableElements = courseElements.parents().parents().next()[index].select("table.datadisplaytable tr:has(td)")
            }
            index += 1

            var lecturer: String

            for (tableElem in tableElements) { //REPEATED PER LESSON
                val time = tableElem.select("td:nth-of-type(2)").text()
                val dayUnformatted: String = tableElem.select("td:nth-of-type(3)").text()
                val unparsedLocation = tableElem.select("td:nth-of-type(4)").text()
                val location = parseLocation(unparsedLocation)
                lecturer = tableElem.select("td:nth-of-type(7)").text()
                Log.d("LOCATION", location)
                val day = dayMap.getOrDefault(dayUnformatted, "TBA")

                if (theNewSection.lecturer == null) {
                    theNewSection.lecturer = lecturer
                }


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
                    theNewSection.courseCode = courseCode
                    courseNewList.last().addMainSection(theNewSection)
                }
                else { // New code, this is a new course - create it
                    val theNewCourse = Course(courseName, courseCode)
                    theNewSection.courseCode = courseCode
                    theNewCourse.addMainSection(theNewSection)
                    courseNewList.add(theNewCourse)
                    lastCourse = courseCode
                }
            }
            else if (courseCode.last() == 'R' || courseCode.last() == 'N'){ //'N' for CIP
                // This is a recit section - add to the prev course
                theNewSection.courseCode = courseNewList.last().courseCode
                courseNewList.last().addRecitSection(theNewSection)
            }
            else {
                // This is a lab section - add to lab sections
                theNewSection.courseCode = courseNewList.last().courseCode
                courseNewList.last().addLabSection(theNewSection)
            }

        }

        return courseNewList
    }

    // Get course codes - CS
    suspend fun fetchCourseCodes(date: String): List<String> {
        return ScrapeService.fetchCourseCodes(date)
    }

    // Get courses - CS201
    suspend fun fetchAllCourses(date: String, courses: List<String>): Document {
        return ScrapeService.fetchAllCourses(date, courses)
    }

    private fun parseLocation(location: String): String {
        val tokens = location.split(" ")
        return when {
            location.contains("Arts and Social") -> "FASS ${tokens.last()}"
            location.contains("Business") -> "FMAN ${tokens.last()}"
            location.contains("Engin.") -> "FENS ${tokens.last()}"
            location.contains("To Be Announced") -> "TBA"
            location.isEmpty() -> "TBA"
            location.contains("University Center") -> "UC ${tokens.last()}"
            location.contains("Languages") -> "SoL ${tokens.last()}"
            else -> location
        }
    }
}
