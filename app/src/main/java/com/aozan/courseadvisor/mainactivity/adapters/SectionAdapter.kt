package com.aozan.courseadvisor.mainactivity.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.aozan.courseadvisor.databinding.ItemSectionBinding
import com.aozan.courseadvisor.model.Section

class SectionAdapter(private val sections: List<Section>, private var itemClickCallback: ((section: Section, isToggled: Boolean) -> Unit)) :
    RecyclerView.Adapter<SectionAdapter.SectionViewHolder>() {

    inner class SectionViewHolder(private val binding: ItemSectionBinding) : RecyclerView.ViewHolder(binding.root) {
        var section: Section? = null

        init {
            binding.toggleButton.setOnCheckedChangeListener{ compoundButton: CompoundButton, isChecked: Boolean ->
                if(compoundButton.isPressed) {
                    for (otherSection in sections) {
                        if (otherSection.crn != section!!.crn && otherSection.isToggled) {
                            otherSection.isToggled = false
                            itemClickCallback(otherSection, false)
                        }
                    }
                    section?.let {
                        it.isToggled = isChecked
                        notifyDataSetChanged()
                        itemClickCallback(it, isChecked)
                    }
                }
            }
        }

        fun bind(section: Section) {
            binding.sectionCode.text = section.sectionCode
            binding.sectionProfs.text = section.lecturer
            val theTimes = getTimes(section)
            binding.classTimes.text = theTimes
            binding.toggleButton.isChecked = section.isToggled
        }

        fun getTimes(section: Section): String {
            var isFirst = true

            val stringBuilder = StringBuilder()
            val lessons = section.lesson

            for (lesson in lessons) {
                if(!isFirst) {
                    stringBuilder.append("\n")
                }
                isFirst = false

                stringBuilder.append(lesson.day)
                if (lesson.timeStart.isNullOrEmpty()) {
                    stringBuilder.append(" TBA")
                }
                else {
                    stringBuilder.append(" ")
                    stringBuilder.append(lesson.timeStart)
                    stringBuilder.append(" - ")
                    stringBuilder.append(lesson.timeEnd)
                }
                stringBuilder.append(" ")
                stringBuilder.append(lesson.location)
            }

            return stringBuilder.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding = ItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        holder.section = sections[position]
        holder.bind(sections[position])
    }

    override fun getItemCount(): Int = sections.size
}

