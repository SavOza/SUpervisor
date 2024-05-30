package com.aozan.courseadvisor.mainactivity

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aozan.courseadvisor.databinding.ActivityMainBinding
import com.aozan.courseadvisor.gradreqactivity.GradReqActivity
import com.aozan.courseadvisor.mainactivity.adapters.CoursesAdapter
import com.aozan.courseadvisor.misc.CourseTextsView
import com.aozan.courseadvisor.model.Course
import com.aozan.courseadvisor.model.Section
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var adapter: CoursesAdapter
    private lateinit var recView: RecyclerView

    private var trueCourseHeight: Int = 0 // this will be set to the height of a course in the schedule depending on the user's screen size
    private val mainScope = MainScope()

    private var data: List<Course> = listOf()

    private val dayToIdx = mapOf(
        "MON" to 0,
        "TUE" to 1,
        "WED" to 2,
        "THU" to 3,
        "FRI" to 4
    )

    private val backgroundColors = listOf(
        "#8A2BE2", "#A52A2A", "#5F9EA0", "#D2691E", "#FF7F50", "#00008B",
        "#008B8B", "#B8860B", "#006400", "#8B008B", "#FF8C00", "#8B0000",
        "#483D8B", "#9400D3", "#FF1493", "#00BFFF", "#228B22", "#DAA520",
        "#4B0082", "#800000", "#BA55D3", "#3CB371", "#7B68EE", "#C71585",
        "#191970", "#FF4500", "#CD853F", "#800080", "#4169E1", "#8B4513",
        "#2E8B57", "#A0522D", "#6A5ACD", "#4682B4", "#D2B48C", "#008080",
        "#40E0D0", "#EE82EE", "#9ACD32"
    )


    private val viewsList: MutableMap<String, MutableSet<CourseTextsView>> = mutableMapOf()
    private val courseColors: MutableMap<String, Int> = mutableMapOf()
    private val scheduledCourses: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        trueCourseHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 34F, Resources.getSystem().displayMetrics).toInt() //convert 34dp to pixels according to size

        recView = binding.sidebar.recView
        recView.layoutManager = LinearLayoutManager(this)

        adapter = CoursesAdapter(data, fun(section: Section, isToggled: Boolean) {
            if (isToggled) {
                addSectionToSched(section)
            }
            else {
               removeItemsFromSched(section)
            }
        })

        recView.adapter = adapter

        setOnClicks()
    }

    private fun addSectionToSched(section: Section) {
        for (lesson in section.lesson) {
            if (lesson.timeStart == null || lesson.timeEnd == null || lesson.day.isEmpty()) { //If the date is "TBA", cant place on schedule
                continue
            }

            val startInt = lesson.timeStart.substringBefore(":").trim().toInt()
            val endInt = lesson.timeEnd.substringBefore(":").trim().toInt()

            val startIdx = if (startInt >= 8) startInt - 8 else startInt + 4
            val endIdx = if (endInt >= 8) endInt - 9 else endInt + 3

            val row_indexes = (startIdx..endIdx).toList()
            val col_index = dayToIdx[lesson.day]

            val fullSectionName = section.fullSectionName
            val sectionCode = section.sectionCode
            val viewName = "$fullSectionName-$sectionCode"

            // For importing schedule to grad requirements, this will be saved to sharedpref
            if (!scheduledCourses.contains(section.courseCode)) {
                scheduledCourses.add(section.courseCode)
            }

            for (row in row_indexes) {
                var theColor: Int = 0
                if (courseColors.contains(section.courseCode)) {
                    theColor = courseColors[section.courseCode]!!
                }
                else {
                    theColor = Random.nextInt(backgroundColors.size)
                    courseColors[section.courseCode] = theColor
                }

                addItemToCell(section.crn, viewName.trim(), lesson.location.trim(), backgroundColors[theColor], row, col_index!!)
            }
        }
    }

    private fun removeItemsFromSched(section: Section) {
        val crn = section.crn
        val viewsSet = viewsList[crn]
        if (viewsSet != null) {
            for (view in viewsSet) {
                val linLayout = view.parent as LinearLayout
                linLayout.removeView(view)
            }
            viewsList.remove(crn)
        }
    }

    private fun setOnClicks() { //Set onclicklisteners of each element
        binding.icMenu.setOnClickListener {
                binding.mainDrawerLayout.openDrawer(GravityCompat.START)
        }

        binding.icGrad.setOnClickListener {
            val intent = Intent(this, GradReqActivity::class.java)
            startActivity(intent)
        }

        binding.sidebar.btnAdvancedfilter.setOnClickListener {
            val prev = supportFragmentManager.findFragmentByTag("chooseFilterDialog")
            if (prev == null){
                val dialog = ChooseFilterDialogFragment()
                dialog.show(supportFragmentManager, "chooseFilterDialog")
            }
        }

        binding.icRefresh.setOnClickListener {
            mainScope.launch {
                try {
                    data = viewModel.executeService("202203")
                    adapter.updateData(data)
                }
                catch (e: Exception) {
                    e.message?.let { it1 -> Log.d("ERROR", it1) }
                    Toast.makeText(this@MainActivity, "Failed to get data, check network connection", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.sidebar.searchBar.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun addItemToCell(crn: String, courseName: String, className: String, color: String, rowNo: Int, colNo: Int) {
        val index = 6 * (rowNo + 1) + (colNo + 1)

        val myNewView: CourseTextsView = CourseTextsView(this)
        myNewView.setCourseName(courseName)
        myNewView.setClassName(className)
        myNewView.setBackground(Color.parseColor(color))


        val theLinearLayout: LinearLayout? = binding.mainGrid.getChildAt(index) as? LinearLayout

        theLinearLayout?.let {
            theLinearLayout.addView(myNewView)
            theLinearLayout.requestLayout()
            theLinearLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

            val newHeight = theLinearLayout.measuredHeight


            theLinearLayout.post {
                val measuredHeight = it.measuredHeight

                if (newHeight > measuredHeight) {
                    val params = theLinearLayout.layoutParams
                    val oldHeight = params.height
                    params.height = oldHeight + (newHeight - measuredHeight) + 25
                    theLinearLayout.layoutParams = params

                    val params2 = theLinearLayout.layoutParams

                }
            }

            if (viewsList[crn] == null) {
                val viewList = mutableSetOf(myNewView)
                viewsList[crn] = viewList
            }
            else {
                viewsList[crn]!!.add(myNewView)
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

    override fun onPause() {
        super.onPause()

        val sharedPrefs = getSharedPreferences("courseAdvisorSharedPref", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        val scheduledSet = scheduledCourses.toSet()

        editor.putStringSet("scheduledCourses", scheduledSet)
        editor.apply()
    }
}