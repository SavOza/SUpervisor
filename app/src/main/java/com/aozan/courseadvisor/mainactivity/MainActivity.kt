package com.aozan.courseadvisor.mainactivity

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aozan.courseadvisor.R
import com.aozan.courseadvisor.databinding.ActivityMainBinding
import com.aozan.courseadvisor.gradreqactivity.GradReqActivity
import com.aozan.courseadvisor.misc.CourseTextsView
import com.aozan.courseadvisor.model.Course
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        trueCourseHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 34F, Resources.getSystem().displayMetrics).toInt() //convert 34dp to pixels according to size

        recView = binding.sidebar.recView
        recView.layoutManager = LinearLayoutManager(this)
        adapter = CoursesAdapter(data)
        recView.adapter = adapter

        setOnClicks()
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


        binding.sidebar.btnAddcourse.setOnClickListener {
            val rowNo = binding.sidebar.rowSpinner.selectedItem as? String
            val colNo = binding.sidebar.colSpinner.selectedItem as? String

            //TODO:
            // change to dynamic index
            // add dynamic height changing
            // call additemtocell
            // try clearing board

            if (rowNo != null && colNo != null) {
                mainScope.launch {
                    data = viewModel.executeService(binding.sidebar.inputText.text.toString())

                    adapter.updateData(data)


                    Log.d("THEVERYEND", data.last().toString())
                }

                //addItemToCell(rowNo.toInt(), colNo.toInt())
            }
        }
    }

    private fun addItemToCell(rowNo: Int, colNo: Int) {
        val index = 6 * rowNo + colNo

        val myNewView: CourseTextsView = CourseTextsView(this)
        myNewView.setClassName("New class")
        myNewView.setCourseName("New course")
        myNewView.setBackground(R.color.black)


        val theLinearLayout: LinearLayout? = binding.mainGrid.getChildAt(index) as? LinearLayout

        theLinearLayout?.let {
            theLinearLayout.addView(myNewView)
            theLinearLayout.requestLayout()
            theLinearLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

            val newHeight = theLinearLayout.measuredHeight


            theLinearLayout.post {
                val measuredHeight = it.measuredHeight
                Log.d("DEBUG-MEASURED", it.measuredHeight.toString())

                if (newHeight > measuredHeight) {
                    val params = theLinearLayout.layoutParams
                    val oldHeight = params.height
                    Log.d("DEBUG-OLDHEIGHT", oldHeight.toString())
                    Log.d("DEBUG-FULL", "$oldHeight + ($newHeight - $measuredHeight)")
                    params.height = oldHeight + (newHeight - measuredHeight) + 25
                    theLinearLayout.layoutParams = params

                    val params2 = theLinearLayout.layoutParams
                    val newHeight2 = params2.height
                    Log.d("DEBUG-NEWHEIGHT", newHeight2.toString())

                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}