package com.example.jadwalpelajaranxd

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_SCHEDULE (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DAY TEXT,
                $COLUMN_SUBJECT TEXT,
                $COLUMN_START_TIME TEXT,
                $COLUMN_END_TIME TEXT,
                $COLUMN_TEACHER TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SCHEDULE")
        onCreate(db)
    }


    fun insertSchedule(day: String, subject: String, startTime: String, endTime: String, teacher: String?): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DAY, day)
            put(COLUMN_SUBJECT, subject)
            put(COLUMN_START_TIME, startTime)
            put(COLUMN_END_TIME, endTime)
            put(COLUMN_TEACHER, teacher)
        }
        val result = db.insert(TABLE_SCHEDULE, null, values)
        db.close()
        return result
    }


    fun updateSchedule(id: Int, day: String, subject: String, startTime: String, endTime: String, teacher: String?): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DAY, day)
            put(COLUMN_SUBJECT, subject)
            put(COLUMN_START_TIME, startTime)
            put(COLUMN_END_TIME, endTime)
            put(COLUMN_TEACHER, teacher)
        }
        val result = db.update(TABLE_SCHEDULE, values, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return result
    }


    fun deleteSchedule(id: Int): Int {
        val db = writableDatabase
        val result = db.delete(TABLE_SCHEDULE, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return result
    }


    fun getAllSchedules(): List<ScheduleModel> {
        val scheduleList = mutableListOf<ScheduleModel>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_SCHEDULE ORDER BY $COLUMN_DAY", null)

        if (cursor.moveToFirst()) {
            do {
                val schedule = ScheduleModel(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    day = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY)),
                    subject = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBJECT)),
                    startTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_TIME)),
                    endTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_TIME)),
                    teacher = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER))
                )
                scheduleList.add(schedule)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return scheduleList
    }

    companion object {
        private const val DATABASE_NAME = "jadwal_pelajaran.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_SCHEDULE = "schedule"
        const val COLUMN_ID = "id"
        const val COLUMN_DAY = "day"
        const val COLUMN_SUBJECT = "subject"
        const val COLUMN_START_TIME = "start_time"
        const val COLUMN_END_TIME = "end_time"
        const val COLUMN_TEACHER = "teacher"
    }
}
