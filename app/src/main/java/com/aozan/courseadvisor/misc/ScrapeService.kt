package com.aozan.courseadvisor.misc

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object ScrapeService {

    // Get all available course codes (CS, EE, IE...)
    suspend fun fetchCourseCodes(date: String): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                val optionsList = mutableListOf<String>()

                val document = Jsoup.connect("https://suis.sabanciuniv.edu/prod/bwckgens.p_proc_term_date")
                    .data("p_calling_proc","bwckschd.p_disp_dyn_sched")
                    .data("p_term",date)
                    .get()
                    .select("select[name=sel_subj]")

                if (document.isNotEmpty()) {
                    val optionsElement = document.first()!!.select("option")

                    for (optionElement in optionsElement) {
                        val optionText = optionElement.attr("VALUE")

                        optionsList.add(optionText)
                    }
                }

                optionsList
            }
            catch (e: Exception) {
                listOf<String>("ERROR", e.toString())
            }
        }
    }

    // Given course codes, get all actual courses (CS201, CS 210...)
    suspend fun fetchAllCourses(date: String, courses: List<String>): Document {
        return withContext(Dispatchers.IO) {
            try {
                val connection = Jsoup.connect("https://suis.sabanciuniv.edu/prod/bwckschd.p_get_crse_unsec")

                connection.data("term_in", date)

                // I do not know why or how, but every single form data here is necessary for a successful response
                // Blame Sabanci
                connection.data("sel_subj","dummy")
                connection.data("sel_day","dummy")
                connection.data("sel_schd","dummy")
                connection.data("sel_insm","dummy")
                connection.data("sel_camp","dummy")
                connection.data("sel_levl","dummy")
                connection.data("sel_sess","dummy")
                connection.data("sel_instr","dummy")
                connection.data("sel_ptrm","dummy")
                connection.data("sel_attr","dummy")
                connection.data("sel_crse","")
                connection.data("sel_title","")
                connection.data("begin_hh","0")
                connection.data("begin_mi","0")
                connection.data("begin_ap","a")
                connection.data("end_hh","0")
                connection.data("end_mi","0")
                connection.data("end_ap","a")


                for (item in courses) {
                    connection.data("sel_subj", item)
                }

                val theHtml = connection.get()
                theHtml
            }
            catch (e: Exception) {
                Document("abc")
            }
        }
    }
}