package com.example.archerlei.foundationplan.main.model

import com.example.archerlei.foundationplan.base.DEFAULT_FLOAT
import com.example.archerlei.foundationplan.base.DEFAULT_INT
import com.example.archerlei.foundationplan.base.DEFAULT_LONG
import com.example.archerlei.foundationplan.base.DEFAULT_STRING
import java.io.Serializable

/**
 * @description 基础数据
 * Created by archerlei on 2018/12/29
 */
open class FoundationData : Serializable{
    var id = DEFAULT_STRING
    var name = DEFAULT_STRING
    var rangeMin = DEFAULT_FLOAT
    var rangeMax = DEFAULT_FLOAT
    var curPrice = DEFAULT_FLOAT
    var priceTime = DEFAULT_LONG
    var recommendNum = DEFAULT_INT
    var recommendRmb = DEFAULT_INT
}