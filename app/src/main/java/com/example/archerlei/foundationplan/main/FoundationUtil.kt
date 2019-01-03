package com.example.archerlei.foundationplan.main

import android.content.Context
import com.example.archerlei.foundationplan.main.model.FoundationData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @description todo
 * Created by archerlei on 2018/12/29
 */
object FoundationUtil {
    private const val FOUNDATION_KEY = "foundation"
    private const val UPDATE_RANGE_TIME = "update_range_time"

    fun storeFoundationList(context: Context, data: List<FoundationData>) {
        val sp = context.getSharedPreferences(FOUNDATION_KEY, Context.MODE_PRIVATE)
        sp.edit().putString(FOUNDATION_KEY, Gson().toJson(data)).apply()
    }

    fun readFoundationList(context: Context): List<FoundationData> {
        val sp = context.getSharedPreferences(FOUNDATION_KEY, Context.MODE_PRIVATE)
        val json = sp.getString(FOUNDATION_KEY, null)
        val type = (object :TypeToken<ArrayList<FoundationData>>(){}).type
        return Gson().fromJson(json, type) ?: ArrayList()
    }
}
