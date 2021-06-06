package com.viet.mydiary.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.viet.mydiary.model.Events
import com.viet.mydiary.utils.coverD
import com.viet.mydiary.utils.coverDate
import com.viet.mydiary.utils.textToDate
import java.text.SimpleDateFormat
import java.util.*

class DBOpenHelper(context: Context) : SQLiteOpenHelper(
    context,
    DB_NAME, null,
    DB_VERSION
) {

    private val CREATE_EVENTS_TABLE =
        "create table $EVENT_TABLE_NAME ($ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$TITLE TEXT, $EVENT TEXT, $DATE TEXT)"

    private val DROP_EVENTS_TABLE = "DROP TABLE IF EXISTS $EVENT_TABLE_NAME"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_EVENTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL(DROP_EVENTS_TABLE)
            onCreate(db)
        }
    }

    fun allEvent(): MutableList<Events> {
        val listEvents: MutableList<Events> = mutableListOf()
        val selectQuery = "SELECT * FROM $EVENT_TABLE_NAME"
        val db: SQLiteDatabase = this.writableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val event = Events(
                    cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getString(cursor.getColumnIndex(TITLE)),
                    cursor.getString(cursor.getColumnIndex(EVENT)),
                    textToDate(cursor.getString(cursor.getColumnIndex(DATE)))
                )
                listEvents.add(event)
            } while (cursor.moveToNext())
        }
        db.close()
        return listEvents
    }

    fun saveEvent(event: Events) {
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(TITLE, event.title)
        values.put(EVENT, event.event)
        values.put(DATE, coverDate(event.date))

        db.insert(EVENT_TABLE_NAME, null, values)
        db.close()
    }

    fun updateEvent(event: Events) {
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(TITLE, event.title)
        values.put(EVENT, event.event)
        values.put(DATE, coverDate(event.date))
        db.update(EVENT_TABLE_NAME, values, "$ID=?", arrayOf(event.id.toString()))
        db.close()
    }

    fun deleteEvent(event: Events) {
        val db: SQLiteDatabase = this.writableDatabase
        db.delete(EVENT_TABLE_NAME, "$ID=?", arrayOf(event.id.toString()))
        db.close()
    }

    fun readEvents(date: Date?): MutableList<Events> {
        val listEvents: MutableList<Events> = mutableListOf()
        val selectQuery = "SELECT * FROM $EVENT_TABLE_NAME"
        val db: SQLiteDatabase = this.writableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val event = Events(
                    cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getString(cursor.getColumnIndex(TITLE)),
                    cursor.getString(cursor.getColumnIndex(EVENT)),
                    textToDate(cursor.getString(cursor.getColumnIndex(DATE)))
                )
                listEvents.add(event)
            } while (cursor.moveToNext())
        }
        db.close()
        return if (date == null)
            listEvents
        else {
            val listE: MutableList<Events> = mutableListOf()
            for (i in 0 until listEvents.size) {
                if (coverD(listEvents[i].date) == coverD(date))
                    listE.add(listEvents[i])
            }
            listE
        }
    }

    fun deleteAll() {
        val db: SQLiteDatabase = this.writableDatabase
        db.execSQL("DELETE FROM $EVENT_TABLE_NAME")
        db.close()
    }

    companion object {
        private val DB_VERSION = 1
        private val DB_NAME = "EVENTS_DB"
        private val EVENT_TABLE_NAME = "eventstable"
        private val ID = "id"
        private val TITLE = "title"
        private val EVENT = "event"
        private val DATE = "date"
    }
}