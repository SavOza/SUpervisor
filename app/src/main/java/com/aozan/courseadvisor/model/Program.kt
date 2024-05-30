package com.aozan.courseadvisor.model

data class Program (val programName: String, val ucReq: Int, val rcReq: Int, val ceReq: Int,
                    val aeReq: Int, val feReq: Int, val facReq: Int, val sciReq: Int,
                    val engReq: Int, val suReq: Int, val ectsReq: Int){

    var ucTaken: Int = 0
    var rcTaken: Int = 0
    var ceTaken: Int = 0
    var aeTaken: Int = 0
    var feTaken: Int = 0
    var facTaken: Int = 0
    var sciTaken: Int = 0
    var engTaken: Int = 0
    var suTaken: Int = 0
    var ectsTaken: Int = 0

    fun resetTaken() {
        ucTaken = 0
        rcTaken = 0
        ceTaken = 0
        aeTaken = 0
        feTaken = 0
        facTaken = 0
        sciTaken = 0
        engTaken = 0
        suTaken = 0
        ectsTaken = 0
    }
}