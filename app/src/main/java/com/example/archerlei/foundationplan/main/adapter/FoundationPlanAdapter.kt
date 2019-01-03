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
class FoundationPlanAdapter(context: Context): RecyclerView.Adapter<FoundationPlanAdapter.ViewHolder>() {

    private val mLayoutInflater = LayoutInflater.from(context)
    private val mList = ArrayList<FoundationData>()
    private val mDefaultDecimalFormat = DecimalFormat("0.000")

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
        private val range = itemView.findViewById<TextView>(R.id.foundation_range)
        private val price = itemView.findViewById<TextView>(R.id.foundation_price)
        private val recommend = itemView.findViewById<TextView>(R.id.foundation_recommend)

        @SuppressLint("SetTextI18n")
        fun setData(position: Int) {
            val item = mList.getOrNull(position)?:return
            name.text = item.name
            range.text = "${mDefaultDecimalFormat.format(item.rangeMin)} - ${mDefaultDecimalFormat.format(item.rangeMax)}"
            price.text = item.curPrice.toString()
            recommend.text = "推荐买${item.recommendNum}份，${item.recommendRmb}元"
        }
    }
}