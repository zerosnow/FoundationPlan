package com.example.archerlei.foundationplan.main

import android.content.Context
import com.example.archerlei.foundationplan.main.model.FoundationData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

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

    fun clearFoundationList(context: Context) {
        val sp = context.getSharedPreferences(FOUNDATION_KEY, Context.MODE_PRIVATE)
        sp.edit().remove(FOUNDATION_KEY).apply()
    }

    fun getFoundationSimpleName(id: String): String {
        return when(id) {
            "519697" -> "交银优势行业"
            "007531" -> "华宝券商"
            "160424" -> "华安创业50"
            "003986" -> "申万中证500"
            "110003" -> "易方达上证50"
            "163406" -> "兴全和润"
            "005827" -> "易方达蓝筹"
            "090010" -> "中证红利"
            "161723" -> "中证银行"
            "012832" -> "新能源etf"
            "008888" -> "半导体etf"
            "001717" -> "工银医疗"
            else -> ""
        }

    }
}
