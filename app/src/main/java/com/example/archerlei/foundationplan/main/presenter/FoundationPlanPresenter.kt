package com.example.archerlei.foundationplan.main.presenter

import android.annotation.SuppressLint
import android.util.Log
import com.android.volley.Response
import com.example.archerlei.foundationplan.base.Gb2312StringRequest
import com.example.archerlei.foundationplan.base.Global
import com.example.archerlei.foundationplan.base.Utf8StringRequest
import com.example.archerlei.foundationplan.main.FoundationUtil
import com.example.archerlei.foundationplan.main.model.FoundationData
import com.example.archerlei.foundationplan.main.view.IFoundationPlanView
import com.google.gson.Gson
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.ArrayList

/**
 * @description 定投计划页presenter
 * Created by archerlei on 2018/12/29
 */
class FoundationPlanPresenter(private val view: IFoundationPlanView) {
    companion object {
        private const val TAG = "FoundationPlanPresenter"
        private const val DEFAULT_WEEK_RMB = 500
    }

    private val mList = ArrayList<FoundationData>()
    private val waitCount = AtomicInteger(0)
    init {

    }

    fun initData() {
        mList.addAll(FoundationUtil.readFoundationList(view.getActivity()))
        //https://docs.qq.com/sheet/DY2x2UlJ3TUVVYU9n?tab=n7coyn 2023/8/18默认值
        addData("110003", 1.7f)
        addData("003986", 1.7f)
        addData("090010", 2.1f)
        addData("007531", 1.2f)
        addData("163406", 1.5f)
        addData("519697", 4.5f)
        addData("001717", 3.0f)
        addData("217002", 1.3f)
        addData("161723", 1.1f)
        addData("160424", 1.45f)
        addData("005827", 1.9f)
        addData("012832", 0.5f)
        addData("008888", 0.8f)
    }

    fun addData(code: String, baseLine: Float) {
        if (mList.find { it.id == code } != null) return
        val foundationData = FoundationData()
        foundationData.id = code
        foundationData.baseLine = baseLine
        mList.add(foundationData)
        writeToDisk()
        requestData(code)
    }

    fun requestData(code: String? = null) {
        if (code != null) {
            sendRequest1(code)
            sendRequest2(code)
        } else {
            for (data in mList) {
                sendRequest1(data.id)
                sendRequest2(data.id)
            }
        }

    }

    fun updateRange(up: Boolean, position: Int) {
        mList.getOrNull(position)?.let {
            it.baseLine += if (up) 0.01f else -0.01f
        }
        calculateAndUpdateUI()
    }

    fun deleteItem(position: Int) {
        if (position < 0 || position >= mList.size) {
            return
        }
        mList.removeAt(position)
        calculateAndUpdateUI()
    }

    @SuppressLint("SimpleDateFormat")
    private fun sendRequest1(id: String) {
        var url = "https://fundgz.1234567.com.cn/js/_id.js?rt=_timeStamp"
        val timeStamp = System.currentTimeMillis().toString()
        url = url.replace("_id", id).replace("_timeStamp", timeStamp)

        val request = Utf8StringRequest(url, { response ->
            val json = response.substring(8, response.length - 2)
            val map = Gson().fromJson(json, HashMap<String, String>().javaClass)
            mList.find {it.id == id}?.let {
                val priceTime = try {
                    SimpleDateFormat("yyyy-MM-dd hh:mm").parse(map["gztime"]).time
                } catch (e :Exception) {
                    0L
                }

                val price = map["gsz"]?.toFloat()?:0f
                it.todayPercent = map["gszzl"]?:""
                it.name = map["name"]?:""
                if (priceTime > it.priceTime) {
                    it.curPrice = price
                    it.priceTime = priceTime
                }
                subAndTryUpdateUI()
            }
        }, {
            subAndTryUpdateUI()
        })

        waitCount.incrementAndGet()
        Global.mRequestQueue.get(view.getActivity()).add(request)
    }

    @SuppressLint("SimpleDateFormat")
    private fun sendRequest2(id: String) {
        var url = "https://www.howbuy.com/fund/fundtool/calreturnajax.htm?code=_id&ds=_date&act=1"
        val date = SimpleDateFormat("yyyyMMdd").format(Date())
        url = url.replace("_id", id).replace("_date", date)

        val request = Utf8StringRequest(url, { response ->
            val priceTime = try {
                SimpleDateFormat("yyyyMMdd").parse(response.substring(0, 8)).time + 1000 * 3600 * 24
            } catch (e :Exception) {
                0L
            }
            try {
                val price = response.substring(9).toFloat()
                mList.find {it.id == id}?.let {
                    if (priceTime > it.priceTime) {
                        it.curPrice = price
                        it.priceTime = priceTime
                    }
                    subAndTryUpdateUI()
                }
            } catch (e: Exception) {

            }
        }, {
            subAndTryUpdateUI()
        })

        waitCount.incrementAndGet()
        Global.mRequestQueue.get(view.getActivity()).add(request)
    }

    private fun subAndTryUpdateUI() {
        if (waitCount.decrementAndGet() == 0) {
            calculateAndUpdateUI()
        }
    }

    private fun calculateAndUpdateUI() {
        mList.forEach {
            it.offsetPercent = (it.curPrice - it.baseLine) / it.baseLine * 100
        }
        mList.sortBy { it.offsetPercent }
        view.updateList(mList)
        writeToDisk()
    }

    private fun writeToDisk() {
        FoundationUtil.storeFoundationList(view.getActivity(), mList)
    }

    private fun dateToWeek(datetime: String): Int {
        val f = SimpleDateFormat("yyyy-MM-dd")
        val cal = Calendar.getInstance () // 获得一个日历
        try {
            cal.time = f.parse(datetime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return cal.get(Calendar.DAY_OF_WEEK) - 2       //指示一个星期中的某天。
    }

    private fun dateToMonth(datetime: String): Int {
        val f = SimpleDateFormat("yyyy-MM-dd")
        val cal = Calendar.getInstance () // 获得一个日历
        try {
            cal.time = f.parse(datetime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return cal.get(Calendar.DAY_OF_MONTH)       //指示一个星期中的某天。
    }

}