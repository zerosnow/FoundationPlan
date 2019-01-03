package com.example.archerlei.foundationplan.main.presenter

import android.annotation.SuppressLint
import android.widget.Toast
import com.android.volley.Response
import com.example.archerlei.foundationplan.base.Global
import com.example.archerlei.foundationplan.base.Utf8StringRequest
import com.example.archerlei.foundationplan.main.FoundationUtil
import com.example.archerlei.foundationplan.main.model.FoundationData
import com.example.archerlei.foundationplan.main.view.IFoundationPlanView
import com.google.gson.Gson
import java.lang.Exception
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
        private const val DEFAULT_WEEK_RMB = 1000
    }

    private val mList = ArrayList<FoundationData>()
    private val waitCount = AtomicInteger(0)
    init {
        mList.addAll(FoundationUtil.readFoundationList(view.getActivity()))

    }

    fun addData(code: String, rangeMin: Float, rangeMax: Float) {
        if (mList.find { it.id == code } != null) return
        val foundationData = FoundationData()
        foundationData.id = code
        foundationData.rangeMin = rangeMin
        foundationData.rangeMax = rangeMax
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

    fun updateRange() {
        if (waitCount.get() != 0) {
            Toast.makeText(view.getActivity(), "数据未更新，请稍后再点击~", Toast.LENGTH_LONG).show()
            return
        }
        mList.forEach {
            when(it.recommendNum) {
                0 -> {
                    val up = (it.rangeMax - it.rangeMin) / 2
                    it.rangeMin += up
                    it.rangeMax += up
                }
                2 -> {
                    val down = (it.rangeMax - it.rangeMin) / 2
                    it.rangeMin -= down
                    it.rangeMax -= down
                }
            }
        }
        calculateAndUpdateUI()
    }

    fun updateRange(up: Boolean, position: Int) {
        mList.getOrNull(position)?.let {
            var change = (it.rangeMax - it.rangeMin) / 2
            if (!up) change = -change
            it.rangeMin += change
            it.rangeMax += change
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
        var url = "http://fundgz.1234567.com.cn/js/_id.js?rt=_timeStamp"
        val timeStamp = System.currentTimeMillis().toString()
        url = url.replace("_id", id).replace("_timeStamp", timeStamp)

        val request = Utf8StringRequest(url, Response.Listener { response ->
            val json = response.substring(8, response.length - 2)
            val map = Gson().fromJson(json, HashMap<String, String>().javaClass)
            mList.find {it.id == id}?.let {
                val priceTime = try {
                    SimpleDateFormat("yyyy-MM-dd hh:mm").parse(map["gztime"]).time
                } catch (e :Exception) {
                    0L
                }

                val price = map["gsz"]?.toFloat()?:0f
                it.name = map["name"]?:""
                if (priceTime > it.priceTime) {
                    it.curPrice = price
                    it.priceTime = priceTime
                    it.recommendNum = when {
                        it.curPrice < it.rangeMin -> 2
                        it.curPrice > it.rangeMax -> 0
                        else -> 1
                    }
                }
                subAndTryUpdateUI()
            }
        }, Response.ErrorListener {
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

        val request = Utf8StringRequest(url, Response.Listener { response ->
            val priceTime = try {
                SimpleDateFormat("yyyyMMdd").parse(response.substring(0, 8)).time + 1000 * 3600 * 24
            } catch (e :Exception) {
                0L
            }
            val price = response.substring(9).toFloat()
            mList.find {it.id == id}?.let {
                if (priceTime > it.priceTime) {
                    it.curPrice = price
                    it.priceTime = priceTime
                }
                subAndTryUpdateUI()
            }

        }, Response.ErrorListener {
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
        var count = 0
        mList.forEach {
            it.recommendNum = when {
                it.curPrice < it.rangeMin -> 2
                it.curPrice > it.rangeMax -> 0
                else -> 1
            }
            count += it.recommendNum
        }
        if (count != 0) {
            for (data in mList) {
                data.recommendRmb = data.recommendNum * DEFAULT_WEEK_RMB / count
            }
        }
        view.updateList(mList)
        writeToDisk()
    }

    private fun writeToDisk() {
        FoundationUtil.storeFoundationList(view.getActivity(), mList)
    }


}