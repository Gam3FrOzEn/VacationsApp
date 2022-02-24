package com.android.vacationsapp.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.android.vacationsapp.models.Vacation

class DBHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_VACATION)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_VACATION")
        onCreate(db)
    }

    fun insertVacation(vacation: Vacation) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, vacation.name)
        values.put(KEY_HOTEL, vacation.hotel)
        values.put(KEY_LOCATION, vacation.location)
        values.put(KEY_MONEY, vacation.money)
        values.put(KEY_DESC, vacation.description)
        values.put(KEY_IMAGE, vacation.image)

        db.insert(TABLE_VACATION, null, values)
    }

    fun updateVacation(vacation: Vacation) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, vacation.name)
        values.put(KEY_HOTEL, vacation.hotel)
        values.put(KEY_LOCATION, vacation.location)
        values.put(KEY_MONEY, vacation.money)
        values.put(KEY_DESC, vacation.description)
        values.put(KEY_IMAGE, vacation.image)

        val selection = "$KEY_ID = ?"
        val selectionArgs = arrayOf(vacation.id.toString())
        db.update(TABLE_VACATION, values, selection, selectionArgs)
    }

    @get:SuppressLint("Range")
    val vacations: MutableList<Vacation>
        get() {
            val list: MutableList<Vacation> = ArrayList()
            val database = this.readableDatabase
            @SuppressLint("Recycle") val cursor =
                database.query(TABLE_VACATION, columnsVacation, null, null, null, null, null)
            while (cursor.moveToNext()) {
                list.add(
                    Vacation(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_HOTEL)),
                        cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
                        cursor.getString(cursor.getColumnIndex(KEY_MONEY)),
                        cursor.getString(cursor.getColumnIndex(KEY_DESC)),
                        cursor.getString(cursor.getColumnIndex(KEY_IMAGE))
                    )
                )
            }
            return list
        }

    fun deleteVacation(id: String) {
        val db = this.writableDatabase
        val selection = "$KEY_ID = ?"
        val selectionArgs = arrayOf(id)
        db.delete(TABLE_VACATION, selection, selectionArgs)
        db.close()
    }

    companion object {
        private val LOG = DBHelper::class.java.name
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "vacationapp"
        private const val TABLE_VACATION = "vacations"

        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_HOTEL = "hotel"
        private const val KEY_LOCATION = "location"
        private const val KEY_MONEY = "money"
        private const val KEY_DESC = "desc"
        private const val KEY_IMAGE = "image"

        private const val CREATE_TABLE_VACATION = ("CREATE TABLE " + TABLE_VACATION
                + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_HOTEL + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_MONEY + " TEXT,"
                + KEY_DESC + " TEXT,"
                + KEY_IMAGE + " TEXT" + ")")

        var columnsVacation = arrayOf(
            KEY_ID, KEY_NAME, KEY_HOTEL, KEY_LOCATION, KEY_MONEY, KEY_DESC, KEY_IMAGE
        )

    }
}