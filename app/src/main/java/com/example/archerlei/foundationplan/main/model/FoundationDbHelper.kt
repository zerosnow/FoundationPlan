package com.example.archerlei.foundationplan.main.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FoundationDbHelper(context: Context): SQLiteOpenHelper(context, "foundation.db", null, 1) {
    companion object {
        private const val TEXT_TYPE = " TEXT"
        private const val COMMA_SEP = ","
        private const val SQL_CREATE_ENTRIES = "CREATE TABLE ${FoundationContract.FoundationItem.TABLE_NAME}(" +
                "${FoundationContract.FoundationItem._ID} INTEGER PRIMARY KEY, " +
                "${FoundationContract.FoundationItem.COLUMN_NAME_FOUNDATION_DATA} $TEXT_TYPE)"
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FoundationContract.FoundationItem.TABLE_NAME}"
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

}