package com.example.archerlei.foundationplan.main.view

import android.app.Activity
import com.example.archerlei.foundationplan.main.model.FoundationData

/**
 * @description todo
 * Created by archerlei on 2018/12/29
 */
interface IFoundationPlanView {
    fun updateList(list: List<FoundationData>)
    fun getActivity(): Activity
}