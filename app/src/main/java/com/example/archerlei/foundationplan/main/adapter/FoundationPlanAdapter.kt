package com.example.archerlei.foundationplan.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.archerlei.foundationplan.R
import com.example.archerlei.foundationplan.main.model.FoundationData
import java.text.DecimalFormat

/**
 * @description todo
 * Created by archerlei on 2018/12/29
 */
class FoundationPlanAdapter(val context: Context): RecyclerView.Adapter<FoundationPlanAdapter.ViewHolder>() {

    private val mLayoutInflater = LayoutInflater.from(context)
    private val mList = ArrayList<FoundationData>()
    private val mDefaultDecimalFormat3 = DecimalFormat("0.000")
    private val mDefaultDecimalFormat2 = DecimalFormat("0.00")

    fun updateData(list: List<FoundationData>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(mLayoutInflater.inflate(R.layout.foundation_plan_item, parent, false))
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(position)
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.foundation_name)
        private val baseLine = itemView.findViewById<TextView>(R.id.foundation_base_line)
        private val price = itemView.findViewById<TextView>(R.id.foundation_price)
        private val todayPercent = itemView.findViewById<TextView>(R.id.foundation_today_percent)
        private val offset = itemView.findViewById<TextView>(R.id.foundation_offset)

        @SuppressLint("SetTextI18n")
        fun setData(position: Int) {
            val item = mList.getOrNull(position)?:return
            name.text = item.name
            baseLine.text = "基准价：${mDefaultDecimalFormat3.format(item.baseLine)}(${getTradingAdvice(item.offsetPercent)})"
            price.text = item.curPrice.toString()
            todayPercent.text = "(${item.todayPercent}%)"
            todayPercent.setTextColor(if (item.todayPercent.startsWith("-"))
                context.resources.getColor(R.color.colorGreen) else context.resources.getColor(R.color.colorRed))
            offset.text = "${mDefaultDecimalFormat2.format(item.offsetPercent)}%"
            offset.setTextColor(when {
                item.offsetPercent > 0f -> context.resources.getColor(R.color.colorRed)
                item.offsetPercent == 0f -> context.resources.getColor(R.color.colorGray)
                else -> context.resources.getColor(R.color.colorGreen)
            })
        }

        private fun getTradingAdvice(offsetPercent: Float):String {
            return if (offsetPercent <= 0f) {
                "本月建议购买${getBuyNum(offsetPercent)}元"
            } else if (offsetPercent >= 30f){
                "本月建议卖出${getBuyNum(offsetPercent)}元"
            } else {
                "建议观望"
            }
        }

        private fun getBuyNum(offsetPercent: Float): Int {
            return when  {
                offsetPercent < -30f -> 40000
                offsetPercent < -20f -> 30000
                offsetPercent < -10f -> 20000
                offsetPercent < -0f -> 10000
                offsetPercent > 60f -> 40000
                offsetPercent > 40f -> 20000
                offsetPercent >= 20f -> 10000
                else -> 0
            }
        }
    }
}