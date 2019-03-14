package com.example.archerlei.foundationplan.main.model

import android.content.ContentValues
import android.content.Context
import com.google.gson.Gson

class FoundationDbManager(context: Context) {
    private val mDbHelper = FoundationDbHelper(context)

    fun insertData(data: FoundationData) {
        val db = mDbHelper.writableDatabase

        val values = ContentValues()
        values.put(FoundationContract.FoundationItem._ID, data.id)
        values.put(FoundationContract.FoundationItem.COLUMN_NAME_FOUNDATION_DATA, Gson().toJson(data))

        db.insert(FoundationContract.FoundationItem.TABLE_NAME, null, values)
    }

    fun getData(): List<FoundationData> {
        val db = mDbHelper.writableDatabase

        val list = ArrayList<FoundationData>()

//        val cursor = db.query()
        return list
    }

    fun deleteData(data: FoundationData) {
        val db = mDbHelper.writableDatabase



    }
}