package com.aozan.courseadvisor.mainactivity

import android.content.Intent
import android.content.res.Resources
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
import com.aozan.courseadvisor.R
import com.aozan.courseadvisor.databinding.ActivityMainBinding
import com.aozan.courseadvisor.gradreqactivity.GradReqActivity
import com.aozan.courseadvisor.mainactivity.adapters.CoursesAdapter
import com.aozan.courseadvisor.misc.CourseTextsView
import com.aozan.courseadvisor.model.Course
import com.aozan.courseadvisor.model.Section
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


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

    private val viewsList: MutableMap<String, MutableSet<CourseTextsView>> = mutableMapOf()
    private val courseColors: MutableMap<String, Int> = mutableMapOf()

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


            for (row in row_indexes) {
                addItemToCell(section.crn, section.courseCode, lesson.location, R.color.sabanci_blue, row, col_index!!)
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



    private fun addItemToCell(crn: String, courseName: String, className: String, color: Int, rowNo: Int, colNo: Int) {
        val index = 6 * (rowNo + 1) + (colNo + 1)

        val myNewView: CourseTextsView = CourseTextsView(this)
        myNewView.setCourseName(courseName)
        myNewView.setClassName(className)
        myNewView.setBackground(color)


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
}