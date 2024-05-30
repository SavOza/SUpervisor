package com.aozan.courseadvisor.misc

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "coursesdata.db"
        private const val DATABASE_VERSION = 1
        private const val DATABASE_PATH = "/databases/"
    }

    private var dbPath: String = context.applicationInfo.dataDir + DATABASE_PATH

    init {
        val dbFile = context.getDatabasePath(DATABASE_NAME)
        if (!dbFile.exists()) {
            copyDatabase(context)
        }
    }

    private fun copyDatabase(context: Context) {
        try {
            val inputStream: InputStream = context.assets.open(DATABASE_NAME)
            val outputFile = dbPath + DATABASE_NAME
            val outputStream: OutputStream = FileOutputStream(outputFile)

            inputStream.copyTo(outputStream)

            outputStream.flush()
            outputStream.close()
            inputStream.close()
        } catch (e: Exception) {
            throw RuntimeException("The $DATABASE_NAME database couldn't be installed.", e)
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Database already exists
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrade if needed
    }

}
