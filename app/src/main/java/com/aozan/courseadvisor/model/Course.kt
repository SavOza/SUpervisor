package com.aozan.courseadvisor.model

class Course(val courseName: String, val courseCode: String)  {
    private val _mainSections = mutableListOf<Section>()
    val mainSection: List<Section>
        get() = _mainSections

    private val _recitSections = mutableListOf<Section>()
    val recitSections: List<Section>
        get() = _recitSections

    private val _labSections = mutableListOf<Section>()
    val labSections: List<Section>
        get() = _labSections

    fun addMainSection(section: Section) {
        _mainSections.add(section)
    }

    fun addRecitSection(section: Section) {
        _recitSections.add(section)
    }

    fun addLabSection(section: Section) {
        _labSections.add(section)
    }

    override fun toString(): String {
        return courseName
    }
}